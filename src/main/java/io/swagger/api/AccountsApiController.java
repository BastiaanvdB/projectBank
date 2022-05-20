package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.DTO.AccountAbsoluteLimitDTO;
import io.swagger.model.DTO.AccountActivationDTO;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.DTO.AccountPincodeDTO;
import io.swagger.model.ResponseDTO.*;
import io.swagger.model.DTO.DepositDTO;
import io.swagger.model.DTO.WithdrawDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.Account;
import io.swagger.service.AccountService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Accounts")
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ModelMapper modelMapper;

    @Autowired
    private AccountService accountService;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
    }

    public ResponseEntity<AccountResponseDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new account with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody AccountDTO body) {

        // Create Random rnd variable
        Random rnd = new Random();

        // Map dto body to account class
        Account account = this.modelMapper.map(body, Account.class);

        // Generate Iban
        account.setIban(generateIban());

        // Set employer id and activation
        account.setEmployeeId(1);
        account.setActivated(true);

        // Create random 4 digit pin code and then add to db with service
        account.setPin(Integer.valueOf(String.format("%04d", rnd.nextInt(10000))));
        account = accountService.createAccount(account);

        // Map newly created account to response data transfer object and return with http status 201
        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.CREATED);
    }

    private String generateIban() {
        // Get all iban
        String lastIban = accountService.getLastAccount().getIban();

        // Get prefix of this iban
        String prefix = lastIban.substring(0, 9);

        // Get the number of the iban and raise by one, count amount of digits
        int number = Integer.parseInt(lastIban.substring(9)) + 1;
        int amountOfDigits = String.valueOf(number).length();

        // foreach leftover digit place, append a 0
        for (int i = amountOfDigits; i < 9; i++) {
            prefix += '0';
        }

        // Then return the new number and return
        prefix += number;
        return prefix;
    }

    public ResponseEntity<List<DepositResponseDTO>> createDeposit(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "Post a deposit to this endpoint", required=true, schema=@Schema()) @Valid @RequestBody DepositDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<DepositResponseDTO>>(objectMapper.readValue("[ {\n  \"amount\" : 150,\n  \"iban\" : \"NLxxABNAxxxxxxxxxx\",\n  \"location\" : \"ATM Haarlem\",\n  \"iat\" : 1650466380\n}, {\n  \"amount\" : 150,\n  \"iban\" : \"NLxxABNAxxxxxxxxxx\",\n  \"location\" : \"ATM Haarlem\",\n  \"iat\" : 1650466380\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<DepositResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<DepositResponseDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<WithdrawResponseDTO>> createWithdraw(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("IBAN") String IBAN,@Parameter(in = ParameterIn.DEFAULT, description = "Post a withdraw to this endpoint", required=true, schema=@Schema()) @Valid @RequestBody WithdrawDTO body) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<WithdrawResponseDTO>>(objectMapper.readValue("[ {\n  \"amount\" : 150,\n  \"iban\" : \"NLxxABNAxxxxxxxxxx\",\n  \"location\" : \"ATM Haarlem\",\n  \"iat\" : 1650466380\n}, {\n  \"amount\" : 150,\n  \"iban\" : \"NLxxABNAxxxxxxxxxx\",\n  \"location\" : \"ATM Haarlem\",\n  \"iat\" : 1650466380\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<WithdrawResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<WithdrawResponseDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<AccountResponseDTO> getAccountByIban(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("iban") String iban) {

        // When length is not 18, throw illegal argument exception
        if (iban.length() != 18) {
            throw new IllegalArgumentException();
        }

        // Get the account, create mapper
        Account account = accountService.getOneByIban(iban);

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Use mapper to map account to account response data transfer object
        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);

        // Return the account dto and http 200
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "status", required = false) String status) {

        // Get all accounts from service
        List<Account> accounts = accountService.getAll();

        // use mapper to map all accounts to user response data transfer object
        List<AccountResponseDTO> responseDTOS = accounts.stream().map(account -> this.modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());

        // return all response dto's and http 200
        return new ResponseEntity<List<AccountResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    public ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsFromAccount(@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator,@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance) {
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

    public ResponseEntity<BigDecimal> setAccountLimit(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the Absolute Limit of a existing account with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody AccountAbsoluteLimitDTO body) {

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // Set the limit with value from body and update the account
        account.setAbsoluteLimit(body.getAbsoluteLimit());
        accountService.updateLimit(account);

        // Return http status 200
        return new ResponseEntity<BigDecimal>(account.getAbsoluteLimit(), HttpStatus.OK);
    }

    public ResponseEntity<Integer> setAccountPin(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Change the pincode of a existing account with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody AccountPincodeDTO body) {

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // Set the pin with value from body and update account
        account.setPin(Integer.valueOf(body.getNewPincode()));
        accountService.updatePin(account);

        // return http status 200
        return new ResponseEntity<Integer>(account.getPin(), HttpStatus.OK);
    }

    public ResponseEntity<Boolean> setAccountStatus(@Size(min=18,max=18) @Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema()) @PathVariable("iban") String iban,@Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing account with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody AccountActivationDTO body) {

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // Set the activation status with value from body and update account
        account.setActivated(body.isActivated());
        accountService.updateStatus(account);

        // return http status 200
        return new ResponseEntity<Boolean>(account.getActivated(), HttpStatus.OK);
    }

}
