package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.entity.Transaction;
import io.swagger.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.threeten.bp.LocalDate;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.ResponseDTO.TransactionResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Transactions")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;
    private ModelMapper modelMapper;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<TransactionResponseDTO> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new tranaction with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody TransactionDTO body) {
        //TODO: Check for savings account
        //todo: If savings account, is it from the same user?
        //TODO: Is ATM? <- special case
        //todo: within absolute limit?
        //todo: within day limit?
        //todo: within transaction limit?
        //todo: is the account from the bank?
        //todo: is account from the same user? then day/transaction limit is no issue


        // Convert request to a Transaction
        Transaction transaction = this.modelMapper.map(body, Transaction.class);

        // Do Transaction and map it to a response DTO
        TransactionResponseDTO responseDTO = this.modelMapper.map(transactionService.createTransaction(transaction), TransactionResponseDTO.class);

        // Return the responseDTO with status OK
        return new ResponseEntity<TransactionResponseDTO>(responseDTO, HttpStatus.OK);
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
            //transactions = transactionService.getAll(query, offset, limit);
        }

        // map the transactions to responseDTO
        //List<TransactionResponseDTO> responseDTOS = transactions.stream().map(transaction -> this.modelMapper.map(transaction, TransactionResponseDTO.class)).collect(Collectors.toList());
        List<TransactionResponseDTO> responseDTOS = null;
        return new ResponseEntity<List<TransactionResponseDTO>>(responseDTOS, HttpStatus.OK);
    }
}
