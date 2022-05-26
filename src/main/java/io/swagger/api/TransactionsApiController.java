package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.TransactionDTO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
    @Autowired
    private TransactionService transactionService;
    private ModelMapper modelMapper;
    private AccountService accountService;
    private JwtTokenProvider tokenProvider;
    private UserService userService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

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

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            //todo: action for employee

            // Check if the iban is from the bank itself
            if (accFrom.getIban() == "NL01INHO0000000001") {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No acces to this account");
            }

            if ((accFrom.getBalance().subtract(transaction.getAmount())).compareTo(accFrom.getAbsoluteLimit()) < 0) {
                // Not enough funds
                throw new ResponseStatusException(HttpStatus.resolve(400), "Not enough money on this account");
            } else {
                // Do Transaction and return response DTO
                return new ResponseEntity<TransactionResponseDTO>(this.doTransaction(transaction, user), HttpStatus.OK);
            }
        }
        if (user == accFrom.getUser()) {
            if (accFrom.getType() == AccountType.SAVINGS || accTo.getType() == AccountType.SAVINGS) {
                if (accFrom.getUser() == accTo.getUser() || accTo.getUser() == accFrom.getUser()) {
                    // Go Further with Savings transaction
                    if ((accFrom.getBalance().subtract(transaction.getAmount())).compareTo(accFrom.getAbsoluteLimit()) < 0) {
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
                if ((accFrom.getBalance().subtract(transaction.getAmount())).compareTo(accFrom.getAbsoluteLimit()) < 0) {
                    // Not enough funds
                    throw new ResponseStatusException(HttpStatus.resolve(400), "You have not enough money on this account");
                } else {
                    //Continue
                    //Check for day limit is not getting exeeded
                    List<Transaction> allTransactionsFromToday = transactionService.getAllFromToday(accFrom.getIban());
                    double daySpendings = 0;
                    for (Transaction trans : allTransactionsFromToday) {
                        daySpendings += trans.getAmount().doubleValue();
                    }
                    if (accFrom.getUser().getDayLimit().compareTo(transaction.getAmount().add(BigDecimal.valueOf(daySpendings))) > 0) {
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

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate, @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN From", required = false) String ibANFrom, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance) {

        List<Transaction> transactions;
        String query = "";

        // parameter options and make 1 query string
        if (offset != null) {
            offset = 0;
        }
        if (limit != null) {
            limit = 50;
        }
        if (ibANFrom != null) {
            if (query != "") query += " AND";
            query += (" ibanFrom = " + ibANFrom);
        }
        if (ibANTo != null) {
            if (query != "") query += " AND";
            query += (" ibanTo = " + ibANTo);
        }
        if (balance != null) {
            if (balanceOperator == null) {
                balanceOperator = ("=");
            }
            if (query != "") query += " AND";
            query += (" amount" + balanceOperator + " " + balance);
        }
        if (startDate != null && endDate != null) {
            Timestamp tsS = Timestamp.valueOf(String.valueOf(startDate.atStartOfDay()));
            Timestamp tsE = Timestamp.valueOf(String.valueOf(endDate.atStartOfDay()));
            if (query != "") query += " AND";
            query += (" iat BETWEEN" + tsS + " AND " + tsE);
        } else if (startDate != null) {
            Timestamp ts = Timestamp.valueOf(String.valueOf(startDate.atStartOfDay()));
            if (query != "") query += " AND";
            query += (" iat >= " + ts);
        } else if (endDate != null) {
            Timestamp ts = Timestamp.valueOf(String.valueOf(endDate.atStartOfDay()));
            if (query != "") query += " AND";
            query += (" iat <= " + ts);
        }

        // if there's no query just get them all with limit and offset 
        if (query != "") {
            transactions = transactionService.getAll(offset, limit);
        } else {
            // get all the transactions with query
            transactions = transactionService.getAll(query, offset, limit);
        }

        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = transactions.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    private TransactionResponseDTO doTransaction(Transaction transaction, User user) {
        // Do Transaction and map it to a response DTO
        transaction.setIssuedBy(user.getId());
        Transaction model = transactionService.createTransaction(transaction);
        // Return the responseDTO
        return this.modelMapper.map(model, TransactionResponseDTO.class);
    }

    private User getUserByToken() {
        // get token and username for further checks
        String token = tokenProvider.resolveToken(request);
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        String username = tokenProvider.getUsername(token);
        return userService.findByEmail(username);
    }

    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DepositResponseDTO> deposit(@RequestBody Transaction body) {
        User user = getUserByToken();
        // from bank to user
        body.setIbanFrom(DEFAULT_BANK_IBAN);
        // do transaction and map it to responseDTO
        TransactionResponseDTO transaction = this.doTransaction(body, user);
        DepositResponseDTO response = this.modelMapper.map(transaction, DepositResponseDTO.class);

        return new ResponseEntity<DepositResponseDTO>(response, HttpStatus.OK);
    }
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WithdrawResponseDTO> withdraw(@RequestBody Transaction body) {
        // from user to bank
        User user = getUserByToken();
        // from bank to user
        body.setIbanTo(DEFAULT_BANK_IBAN);
        // do transaction and map it to responseDTO
        TransactionResponseDTO transaction = this.doTransaction(body, user);
        WithdrawResponseDTO response = this.modelMapper.map(transaction, WithdrawResponseDTO.class);

        return new ResponseEntity<WithdrawResponseDTO>(response, HttpStatus.OK);
    }
}
