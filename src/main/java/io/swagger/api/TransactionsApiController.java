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
import io.swagger.model.entity.Transaction;
import io.swagger.model.exception.*;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.TransactionService;
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
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(tags = "Transactions")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);
    private final ObjectMapper objectMapper;
    private final HttpServletRequest request;
    private ModelMapper modelMapper;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private JwtTokenProvider tokenProvider;

    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<TransactionResponseDTO> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new tranaction with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody TransactionDTO body) throws AccountNotFoundException, InvalidIbanException, UserNotFoundException, ZeroNegativeException, ExcceedsLimitExeption, UnauthorizedException, SameAccountException, InsufficientFundsException, InvalidRoleException, InvalidPincodeException {
        return new ResponseEntity<TransactionResponseDTO>(this.modelMapper.map(this.transactionService.createTransaction(body, getToken()), TransactionResponseDTO.class), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('USER')")
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate, @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN From", required = false) String ibANFrom, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance) throws AccountNotFoundException, InvalidIbanException, UserNotFoundException, UnauthorizedException {

        // get ze transactions
        List<Transaction> transactions = this.transactionService.getAll(startDate, endDate, ibANFrom, ibANTo, balanceOperator, balance, offset, limit, getToken());

        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = transactions.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    // from bank to user
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DepositResponseDTO> createDeposit(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a deposit to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody DepositDTO body) throws AccountNotFoundException, InvalidIbanException, UserNotFoundException, UnauthorizedException {

        DepositResponseDTO response = this.modelMapper.map(this.transactionService.deposit(body, IBAN, getToken()), DepositResponseDTO.class);
        return new ResponseEntity<DepositResponseDTO>(response, HttpStatus.CREATED);
    }

    // from user to bank
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<WithdrawResponseDTO> createWithdraw(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a withdraw to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody WithdrawDTO body) throws AccountNotFoundException, InvalidIbanException, UserNotFoundException, UnauthorizedException {

        WithdrawResponseDTO response = this.modelMapper.map(this.transactionService.withdraw(IBAN, body, getToken()), WithdrawResponseDTO.class);
        return new ResponseEntity<WithdrawResponseDTO>(response, HttpStatus.CREATED);
    }

    // route to other public get transactions
    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsFromAccount(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. ", schema = @Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate, @Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. ", schema = @Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {

        List<Transaction> all = this.transactionService.getAllFromAccount(startDate, endDate, iban, ibANTo, balanceOperator, balance, offset, limit, getToken());
        // map the transactions to responseDTO
        List<TransactionResponseDTO> responseDTOS = all.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }


    // get the day spendings of the provided iban
    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('USER')")
    public ResponseEntity<SpendResponseDTO> getDaySpendings(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {
        return new ResponseEntity<SpendResponseDTO>(this.transactionService.getAllFromTodaySUM(IBAN, getToken()), HttpStatus.OK);
    }

    private String getToken() {
        return tokenProvider.resolveToken(request);
    }
}
