package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.*;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

        // add all user_ids to response as they are saved in user as property
        for (int i = 0; i < responseDTOS.size(); i++) {
            responseDTOS.get(i).setUserId(accounts.get(i).getUser().getId());
        }

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

        // Check if old pincode matches the pincode of the account for validation
        if (!account.getPin().equals(body.getOldPincode())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Wrong pincode!");
        }
        if (!body.getNewPincode().matches("[0-9]+") || body.getNewPincode().length() != 4) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Pincode must only contain 4 digits");
        }

        // Set the pin with value from body and update account
        account.setPin(String.valueOf(body.getNewPincode()));
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

    // **** HELPER METHODS
    private String generatePincode() {

        // Create random pin with 4 digits
        Random rnd = new Random();
        return String.format("%04d", rnd.nextInt(10000));
    }

    private void isValidIban(String iban) {

        if (iban.equals("NL01INHO0000000001")) {
            return;
        }

        // When length is not 18, throw illegal argument exception
        if (iban.length() != 18) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iban must be 18 characters long.");
        }

        // When number section of iban contains letters, throw illegal argument exception
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(iban.substring(10, 18));
        if (!matcher.matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Iban identifier can only contain numbers.");
        }
    }

    private String getUsernameFromBearer() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        return jwtTokenProvider.getUsername(token);
    }

    private boolean canUserPerform(String email) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }

        // Check if username and email equal eachother to make sure, a normal user cannot perform actions on somebody elses account
        if (!jwtTokenProvider.getUsername(token).equals(email) && !jwtTokenProvider.getAuthentication(token).getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
        }
        return true;
    }
}
