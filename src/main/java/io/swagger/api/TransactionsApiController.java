package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Transactions")
public class TransactionsApiController implements TransactionsApi {

    private static final Logger log = LoggerFactory.getLogger(TransactionsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private TransactionService transactionService;

    @org.springframework.beans.factory.annotation.Autowired
    public TransactionsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<TransactionResponseDTO>> createTransaction(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new tranaction with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody TransactionDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<TransactionResponseDTO>>(objectMapper.readValue("[ {\n  \"amount\" : 0.8008281904610115,\n  \"ibanFrom\" : \"NLxxINHO0xxxxxxxxx\",\n  \"issuedBy\" : 1,\n  \"iat\" : 1650466380,\n  \"ibanTo\" : \"NLxxINHO0xxxxxxxxx\"\n}, {\n  \"amount\" : 0.8008281904610115,\n  \"ibanFrom\" : \"NLxxINHO0xxxxxxxxx\",\n  \"issuedBy\" : 1,\n  \"iat\" : 1650466380,\n  \"ibanTo\" : \"NLxxINHO0xxxxxxxxx\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TransactionResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TransactionResponseDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactions(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "The start date for the report. Must be used together with `end_date`. " ,schema=@Schema()) @Valid @RequestParam(value = "start_date", required = false) LocalDate startDate,@Parameter(in = ParameterIn.QUERY, description = "The end date for the report. Must be used together with `start_date`. " ,schema=@Schema()) @Valid @RequestParam(value = "end_date", required = false) LocalDate endDate,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "IBAN From", required = false) String ibANFrom,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<TransactionResponseDTO>>(objectMapper.readValue("[ {\n  \"amount\" : 0.8008281904610115,\n  \"ibanFrom\" : \"NLxxINHO0xxxxxxxxx\",\n  \"issuedBy\" : 1,\n  \"iat\" : 1650466380,\n  \"ibanTo\" : \"NLxxINHO0xxxxxxxxx\"\n}, {\n  \"amount\" : 0.8008281904610115,\n  \"ibanFrom\" : \"NLxxINHO0xxxxxxxxx\",\n  \"issuedBy\" : 1,\n  \"iat\" : 1650466380,\n  \"ibanTo\" : \"NLxxINHO0xxxxxxxxx\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TransactionResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TransactionResponseDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
