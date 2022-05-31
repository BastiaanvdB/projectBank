package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.DepositDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.WithdrawDTO;
import io.swagger.model.ResponseDTO.DepositResponseDTO;
import io.swagger.model.ResponseDTO.TransactionResponseDTO;
import io.swagger.model.ResponseDTO.WithdrawResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
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

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
    }

    // todo: check timestamps

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new tranaction with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody TransactionDTO body) {
        // Convert request to a Transaction
        Transaction transaction = this.modelMapper.map(body, Transaction.class);

        // get accounts for checks
        Account accFrom = accountService.getOneByIban(transaction.getIbanFrom());
        Account accTo = accountService.getOneByIban(transaction.getIbanTo());

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
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No acces to this account");
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
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You have no acces to this account");
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
                    if (accFrom.getUser().getDayLimit().compareTo(transaction.getAmount().add(getDaySpendings(accFrom.getIban()))) > 0) {
                        // Check if the transaction is not exeeding the transaction limit
                        if (accFrom.getUser().getTransactionLimit().compareTo(transaction.getAmount()) > 0) {
                            // Do Transaction and return response DTO
                            return new ResponseEntity<TransactionResponseDTO>(this.doTransaction(transaction, user), HttpStatus.OK);
                        }
                    }
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You have no acces to this account");
        }
        return new ResponseEntity<TransactionResponseDTO>(HttpStatus.FORBIDDEN);
    }

    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN From", required = false) String ibANFrom,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator,
                                                                           @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance) {

        User user = getUserByToken();
        if (user.getRoles().contains(Role.ROLE_USER)) {
            if (!isUserOwner(user, ibANFrom)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You have no acces to this account");
            }
        }
        List<Transaction> transactions;

        // if there's no query just get them all with limit and offset 
        if ((ibANFrom != null) || (ibANTo != null) || (balance != null) || (startDate != null) || (endDate != null)) {
            // get all the transactions with query
            transactions = transactionService.getAll(startDate, endDate, ibANFrom, ibANTo, balanceOperator, balance, offset, limit);
        } else {
            transactions = transactionService.getAll(offset, limit);
        }

        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = transactions.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    //TODO: transactions for user

    // from bank to user
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DepositResponseDTO> createDeposit(@Size(min = 18, max = 18) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a deposit to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody DepositDTO body) {
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
    public ResponseEntity<WithdrawResponseDTO> createWithdraw(@Size(min = 18, max = 18) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a withdraw to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody WithdrawDTO body) {
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
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsFromAccount(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate, @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate) {
        return getAllTransactions(offset, limit, startDate, endDate, iban, ibANTo, balanceOperator, balance);
    }


    // private functions

    // executes the transaction
    private TransactionResponseDTO doTransaction(Transaction transaction, User user) {
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
        // get token and username for further checks
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        String username = tokenProvider.getUsername(token);
        return userService.findByEmail(username);
    }

    // Checks if the user is the owner of the account
    private boolean isUserOwner(User user, String iban) {
        Account acc = accountService.getOneByIban(iban);
        if (acc.getUser() != user) {
            return false;
        } else return true;
    }

    // Check if the account can do this transaction
    private boolean checkBalanceForTransaction(String iban, BigDecimal amount) {
        Account acc = accountService.getOneByIban(iban);
        if ((acc.getBalance().subtract(amount)).compareTo(acc.getAbsoluteLimit()) < 0) {
            return false;
        }
        return true;
    }

    // get the day spendings of the provided iban
    private BigDecimal getDaySpendings(String iban) {
        List<Transaction> allTransactionsFromToday = transactionService.getAllFromToday(iban);
        BigDecimal daySpendings = null;
        for (Transaction trans : allTransactionsFromToday) {
            daySpendings.add(trans.getAmount());
        }
        return daySpendings;
    }
}
