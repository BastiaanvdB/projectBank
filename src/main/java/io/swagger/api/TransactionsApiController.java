package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.DepositDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.WithdrawDTO;
import io.swagger.model.ResponseDTO.DepositResponseDTO;
import io.swagger.model.ResponseDTO.SpendResponseDTO;
import io.swagger.model.ResponseDTO.TransactionResponseDTO;
import io.swagger.model.ResponseDTO.WithdrawResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(tags = "Transactions")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
    private static final String DEFAULT_BANK_IBAN = new String("NL01INHO0000000001");
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private ModelMapper modelMapper;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> createTransaction(
            @Parameter(in = ParameterIn.DEFAULT, description = "Post a new tranaction with this endpoint", required = true, schema = @Schema()) @Valid
            @RequestBody TransactionDTO body) throws AccountNotFoundException, InvalidIbanException {
        // Convert request to a Transaction
        Transaction transaction = this.modelMapper.map(body, Transaction.class);

        // check if the account is the same
        if (transaction.getIbanFrom() == transaction.getIbanTo()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Same account");
        }

        //check if amount <= 0
        if (transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Trying negative or ZERO transaction amount");
        }

        // get accounts for checks
        Account accFrom = accountService.getOneByIban(transaction.getIbanFrom());
        Account accTo = accountService.getOneByIban(transaction.getIbanTo());

        // check if pin is correct
        if (!passwordEncoder.matches(body.getPin(), accFrom.getPin())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Incorrect Pin");
        }

        // get token and username for further checks
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        String username = tokenProvider.getUsername(token);
        User user = userService.findByEmail(username);

        // employee part
        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            // Check if the iban is from the bank itself
            if (accFrom.getIban() == "NL01INHO0000000001") {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No access to this account");
            }

            if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
                // Not enough funds
                throw new ResponseStatusException(HttpStatus.resolve(400), "Not enough money on this account");
            } else {
                // Do Transaction and return response DTO
                return new ResponseEntity<TransactionResponseDTO>(this.doTransaction(transaction, user), HttpStatus.OK);
            }
        }
        // user part
        if (user == accFrom.getUser()) {
            if (accFrom.getType() == AccountType.SAVINGS || accTo.getType() == AccountType.SAVINGS) {
                if (accFrom.getUser() == accTo.getUser() || accTo.getUser() == accFrom.getUser()) {
                    // Go Further with Savings transaction
                    if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
                        // Not enough funds
                        throw new ResponseStatusException(HttpStatus.resolve(400), "Not enough money on this account");
                    } else {
                        // Do Transaction and return response DTO
                        return new ResponseEntity<TransactionResponseDTO>(this.doTransaction(transaction, user), HttpStatus.OK);
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no access to this account");
                }
            } else {
                // Do normal transaction
                //Check if the balance will not exeed the absolute limit with this transaction
                if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
                    // Not enough funds
                    throw new ResponseStatusException(HttpStatus.resolve(400), "You have not enough money on this account");
                } else {
                    //Continue
                    //Check for day limit is not getting exeeded
                    if (accFrom.getUser().getDayLimit().compareTo(transaction.getAmount().add(getDaySpendingsSUM(accFrom.getIban()))) > 0) {
                        // Check if the transaction is not exeeding the transaction limit
                        if (accFrom.getUser().getTransactionLimit().compareTo(transaction.getAmount()) > 0) {
                            // Do Transaction and return response DTO
                            return new ResponseEntity<TransactionResponseDTO>(this.doTransaction(transaction, user), HttpStatus.OK);
                        } else {
                            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This transaction exceeds the transaction limit");
                        }
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "With this transaction day limit will be exceeded");
                    }
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no access to this account");
        }
    }

    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer
                    offset,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer
                    limit,
            @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate
                    startDate,
            @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate
                    endDate,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN From", required = false) String
                    ibANFrom,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String
                    ibANTo,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String
                    balanceOperator,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String
                    balance) throws AccountNotFoundException, InvalidIbanException {

        User user = getUserByToken();
        if (user.getRoles().contains(Role.ROLE_USER)) {
            if (!isUserOwner(user, ibANFrom)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You have no acces to this account");
            }
        }
        // get ze transactions
        List<Transaction> transactions = this.getTransactions(startDate, endDate, ibANFrom, ibANTo, balanceOperator, balance, offset, limit);

        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = transactions.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    // from bank to user
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DepositResponseDTO> createDeposit
    (@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String
             IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a deposit to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody DepositDTO
             body) throws AccountNotFoundException, InvalidIbanException {
        // map the DTO to transaction
        Transaction deposit = this.modelMapper.map(body, Transaction.class);
        // set the iban of the bank on the right posistion
        deposit.setIbanTo(IBAN);

        User user = getUserByToken();

        deposit.setIbanFrom(DEFAULT_BANK_IBAN);
        // do transaction and map it to responseDTO
        TransactionResponseDTO transaction = this.doTransaction(deposit, user);
        DepositResponseDTO response = this.modelMapper.map(transaction, DepositResponseDTO.class);

        return new ResponseEntity<DepositResponseDTO>(response, HttpStatus.OK);
    }

    // from user to bank
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WithdrawResponseDTO> createWithdraw
    (@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String
             IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a withdraw to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody WithdrawDTO
             body) throws AccountNotFoundException, InvalidIbanException {
        // map the DTO to transaction
        Transaction withdraw = this.modelMapper.map(body, Transaction.class);
        // set the iban of the bank on the right posistion
        withdraw.setIbanFrom(IBAN);

        // get user for transaction
        User user = getUserByToken();

        // enough money on account?
        if (!checkBalanceForTransaction(withdraw.getIbanFrom(), withdraw.getAmount())) {
            throw new ResponseStatusException(HttpStatus.resolve(400), "Not enough money on this account");
        }

        withdraw.setIbanTo(DEFAULT_BANK_IBAN);

        // do transaction and map it to responseDTO
        TransactionResponseDTO transaction = this.doTransaction(withdraw, user);
        WithdrawResponseDTO response = this.modelMapper.map(transaction, WithdrawResponseDTO.class);

        return new ResponseEntity<WithdrawResponseDTO>(response, HttpStatus.OK);
    }

    // route to other public get transactions
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsFromAccount(
            @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String
                    iban,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String
                    ibANTo,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String
                    balanceOperator,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String
                    balance,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer
                    offset,
            @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer
                    limit,
            @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid
            @RequestParam(value = "start_date", required = false) LocalDate
                    startDate, @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid
            @RequestParam(value = "end_date", required = false) LocalDate endDate) {

        List<Transaction> all = this.getTransactions(startDate, endDate, iban, ibANTo, balanceOperator, balance, offset, limit);
        all.addAll(this.getTransactions(startDate, endDate, ibANTo, iban, balanceOperator, balance, offset, limit));
        // sort the complete list desc. so that the last transaction is first
        all.sort(Comparator.comparing(Transaction::getIat).reversed());
        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = all.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }


    // get the day spendings of the provided iban
    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('USER')")
    public ResponseEntity<SpendResponseDTO> getDaySpendings(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN) {
        return new ResponseEntity<SpendResponseDTO>(new SpendResponseDTO(getDaySpendingsSUM(IBAN)), HttpStatus.OK);
    }

    // private functions

    // getTransactions
    private List<Transaction> getTransactions(LocalDate startDate, LocalDate endDate, String ibANFrom, String
            ibANTo, String balanceOperator, String balance, Integer offset, Integer limit) {
        // Check if offset and limit is not empty otherwise give default readings
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 50;
        }

        // if there's no query just get them all with limit and offset
        if ((ibANFrom != null) || (ibANTo != null) || (balance != null) || (startDate != null) || (endDate != null)) {
            // get all the transactions with query
            return transactionService.getAll(startDate, endDate, ibANFrom, ibANTo, balanceOperator, balance, offset, limit);
        } else {
            return transactionService.getAll(offset, limit);
        }
    }

    // executes the transaction
    private TransactionResponseDTO doTransaction(Transaction transaction, User user) throws AccountNotFoundException, InvalidIbanException {
        // Do Transaction and map it to a response DTO
        transaction.setIssuedBy(user.getId());
        Transaction model = transactionService.createTransaction(transaction);

        //get from acc and modify it
        Account from = accountService.getOneByIban(transaction.getIbanFrom());
        Account to = accountService.getOneByIban(transaction.getIbanTo());
        // When a account is null, no account was found with specified iban, return 404
        if (from == null || to == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We have no account with this iban");
        }

        from.setBalance(from.getBalance().subtract(transaction.getAmount()));
        accountService.updateBalance(from);

        //get to acc and modify it
        to.setBalance(to.getBalance().add(transaction.getAmount()));
        accountService.updateBalance(to);

        // Return the responseDTO
        return this.modelMapper.map(model, TransactionResponseDTO.class);
    }

    // fetch the user by token
    private User getUserByToken() {
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
        }

        User user = userService.findByEmail(tokenProvider.getUsername(token));

        if (!user.getActivated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
        }

        return user;
    }

    // Checks if the user is the owner of the account
    private boolean isUserOwner(User user, String iban) throws AccountNotFoundException, InvalidIbanException {
        Account acc = accountService.getOneByIban(iban);
        if (acc.getUser() != user) {
            return false;
        } else return true;
    }

    // Check if the account can do this transaction
    private boolean checkBalanceForTransaction(String iban, BigDecimal amount) throws AccountNotFoundException, InvalidIbanException {
        Account acc = accountService.getOneByIban(iban);
        if ((acc.getBalance().subtract(amount)).compareTo(acc.getAbsoluteLimit()) < 0) {
            return false;
        }
        return true;
    }

    // get the day spendings of the provided iban
    private BigDecimal getDaySpendingsSUM(String iban) {
        return transactionService.getAllFromTodaySUM(iban);
    }

}
