package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.*;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(tags = "Accounts")
public class AccountsApiController implements AccountsApi {

    private static final Logger log = LoggerFactory.getLogger(AccountsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    private final ModelMapper modelMapper;

    // constant data of the bank for validation
    private static final String IBAN_BANK = "NL01INHO0000000001";
    private static final String EMAIL_BANK = "bank@live.nl";
    private static final String IBAN_COUNTRY_PREFIX = "NL";
    private static final String IBAN_BANK_PREFIX = "INHO0";

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private TransactionsApiController transactionsApiController;

    @org.springframework.beans.factory.annotation.Autowired
    public AccountsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountDTO body) {

        if (body.getUserId() == null || body.getType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incomplete data.");
        }

        // Map dto body to account class
        Account account = this.modelMapper.map(body, Account.class);
        User employee = userService.findByEmail(getUsernameFromBearer());
        User user = userService.getOne(body.getUserId());

        // User for whom account is being made must exist
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        // Set employee id from bearer
        account.setEmployeeId(employee.getId());

        // send account to service for more data, get the object from db returned
        account = accountService.createAccount(account);

        // Add account to account list of user
        user.setAccounts(new HashSet<Account>(Arrays.asList(account)));
        userService.put(user);

        // Map newly created account to response data transfer object and return with http status 201
        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);
        responseDTO.setUserId(body.getUserId());
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.CREATED);
    }


    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban) {

        // Call validation method to validate the iban given as parameter
        isValidIban(iban);

        // Get the account, create mapper
        Account account = accountService.getOneByIban(iban);

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this iban!");
        }

        // Make sure users can only perform on their own account
        if (!canUserPerform(account.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        // Use mapper to map account to account response data transfer object
        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);
        responseDTO.setUserId(account.getUser().getId());

        // Return the account dto and http 200
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status) {

        List<Account> accounts = null;
        List<Account> filteredAccounts = null;
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 10;
        }

        // when parameter for first or lastname is given, call method for that
        if (firstname != null && firstname.length() > 0 && lastname != null && lastname.length() > 0) {
            accounts = accountService.getAllByFirstAndLastname(firstname, lastname, offset, limit);
        } else if (firstname != null && firstname.length() > 0) {
            accounts = accountService.getAllByFirstname(firstname, offset, limit);
        } else if (lastname != null && lastname.length() > 0) {
            accounts = accountService.getAllByLastname(lastname, offset, limit);
        } else {
            accounts = accountService.getAll(offset, limit);
        }

        // use mapper to map all accounts to user response data transfer object
        List<AccountResponseDTO> responseDTOS = accounts.stream().map(account -> this.modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());

        // remove bank
        responseDTOS.removeIf(dto -> dto.getIban().equals(IBAN_BANK));

        // return all response dto's and http 200
        return new ResponseEntity<List<AccountResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsByUserId(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) {

        // Get all accounts for the user with given id
        List<Account> accounts = accountService.getAllByUserId(userid);

        // When no accounts are returned, no user exists with that id, throw exception with 404
        if (accounts.size() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No accounts found for this user");
        }

        // Make sure users can only perform on their own account
        if (!canUserPerform(accounts.get(0).getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        // Map all accounts to response data transfer objects
        List<AccountResponseDTO> responseDTOS = accounts.stream().map(account -> this.modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());

        // Return the repsonse dto's
        return new ResponseEntity<List<AccountResponseDTO>>(responseDTOS, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<AccountAbsoluteLimitResponseDTO> setAccountLimit(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the Absolute Limit of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountAbsoluteLimitDTO body) {

        // Call validation method to validate the iban given as parameter
        isValidIban(iban);

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this iban!");
        }

        // Make sure users can only perform on their own account
        if (!canUserPerform(account.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        // Set the limit with value from body and update the account
        account.setAbsoluteLimit(body.getAbsoluteLimit());
        accountService.updateLimit(account);

        // Map new value to response dto
        AccountAbsoluteLimitResponseDTO responseDTO = this.modelMapper.map(body, AccountAbsoluteLimitResponseDTO.class);

        // Return http status 200
        return new ResponseEntity<AccountAbsoluteLimitResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<AccountPincodeResponseDTO> setAccountPin(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the pincode of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountPincodeDTO body) {

        // Call validation method to validate the iban given as parameter
        isValidIban(iban);

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this iban!");
        }

        // Make sure users can only perform on their own account
        if (!canUserPerform(account.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }

        // when pincode is wrong and user performing is not employee, throw exception, also throw exception when pincode is not 4 digits
        if (!passwordEncoder.matches(body.getOldPincode(), account.getPin()) && !jwtTokenProvider.getAuthentication(getValidatedToken()).getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Wrong pincode!");
        }
        if (!body.getNewPincode().matches("[0-9]+") || body.getNewPincode().length() != 4) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Pincode must only contain 4 digits");
        }

        // Set the pin with value from body and update account
        account.setPin(passwordEncoder.encode(String.valueOf(body.getNewPincode())));
        accountService.updatePin(account);

        // Map new value to response dto
        AccountPincodeResponseDTO responseDTO = this.modelMapper.map(body, AccountPincodeResponseDTO.class);

        // return http status 200
        return new ResponseEntity<AccountPincodeResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountActivationResponseDTO> setAccountStatus(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountActivationDTO body) {

        // Call validation method to validate the iban given as parameter
        isValidIban(iban);

        // Get the account with iban
        Account account = accountService.getOneByIban(iban);

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this iban!");
        }

        // Set the activation status with value from body and update account
        account.setActivated(body.isActivated());
        accountService.updateStatus(account);

        // Map new value to response dto
        AccountActivationResponseDTO responseDTO = this.modelMapper.map(body, AccountActivationResponseDTO.class);

        // return http status 200
        return new ResponseEntity<AccountActivationResponseDTO>(responseDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<PinAuthenticateResponseDTO> authenticateAcount(PinAuthenticateDTO body) {

        // Call validation method to validate the iban given as parameter
        isValidIban(body.getIban());

        // Get the account with iban
        Account account = accountService.getOneByIban(body.getIban());

        // When account is null, no account was found with specified iban, return 404
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found with this iban!");
        }

        // set values of dto to response dto
        PinAuthenticateResponseDTO responseDTO = this.modelMapper.map(body, PinAuthenticateResponseDTO.class);

        // If pincodes match, set isValid to true and return response dto with http OK
        // Otherwise http unauthorized
        if (passwordEncoder.matches(body.getPincode(), account.getPin())) {
            responseDTO.setIsValid(true);
            return new ResponseEntity<PinAuthenticateResponseDTO>(responseDTO, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "This pin was inccorect");
        }
    }

    // **** HELPER METHODS
    private void isValidIban(String iban) {
        if (iban.equals(IBAN_BANK) && !EMAIL_BANK.equals(this.getUsernameFromBearer())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No access to this account.");
        }
        // When length is not 18, throw illegal argument exception
        if (iban.length() != 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iban must be 18 characters long.");
        }
        // Check the country prefix
        if (!iban.substring(0, 2).equals(IBAN_COUNTRY_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong country prefix, only NL is accepted.");
        }
        // When number section of iban contains letters, throw illegal argument exception
        if (!iban.substring(2, 4).matches("[0-9]+") || !iban.substring(10, 18).matches("[0-9]+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iban identifiers can only contain numbers.");
        }
        // Check the bank prefix
        if (!iban.substring(4, 9).equals(IBAN_BANK_PREFIX)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong bank prefix, only INHO0 is accepted.");
        }
    }

    private boolean canUserPerform(String email) {
        String token = getValidatedToken();

        // Check if username and email equal eachother to make sure, a normal user cannot perform actions on somebody elses account
        if (!jwtTokenProvider.getUsername(token).equals(email) && !jwtTokenProvider.getAuthentication(token).getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        return true;
    }

    private String getValidatedToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        return token;
    }

    private String getUsernameFromBearer() {
        return jwtTokenProvider.getUsername(getValidatedToken());
    }
}
