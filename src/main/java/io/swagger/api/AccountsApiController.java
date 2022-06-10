package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.*;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.model.exception.InvalidPincodeException;
import io.swagger.model.exception.UserNotFoundException;
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

    private static final String IBAN_BANK = "NL01INHO0000000001";

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



    // ** Create account
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountDTO body) throws UserNotFoundException {
        if (body.getUserId() == null || body.getType() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User id and/or account type are missing!");
        }

        Account account = this.modelMapper.map(body, Account.class);
        User employee = userService.findByEmail(getUsernameFromBearer());
        User user = userService.getOne(body.getUserId());

        account = accountService.createAccount(account, employee);
        userService.addAccountToUser(user, account);

        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);
        responseDTO.setUserId(body.getUserId());
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.CREATED);
    }




    // ** Get all accounts for iban
    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<AccountResponseDTO> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban) throws AccountNotFoundException, InvalidIbanException {
        Account account = accountService.getOneByIban(iban);

        // Make sure users can only perform on their own account
        if (!canUserPerform(account.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this endpoint!");
        }

        AccountResponseDTO responseDTO = this.modelMapper.map(account, AccountResponseDTO.class);
        responseDTO.setUserId(account.getUser().getId());
        return new ResponseEntity<AccountResponseDTO>(responseDTO, HttpStatus.OK);
    }




    // ** Get all accounts
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

        if (firstname != null && firstname.length() > 0 && lastname != null && lastname.length() > 0) {
            accounts = accountService.getAllByFirstAndLastname(firstname, lastname, offset, limit);
        } else if (firstname != null && firstname.length() > 0) {
            accounts = accountService.getAllByFirstname(firstname, offset, limit);
        } else if (lastname != null && lastname.length() > 0) {
            accounts = accountService.getAllByLastname(lastname, offset, limit);
        } else {
            accounts = accountService.getAll(offset, limit);
        }

        List<AccountResponseDTO> responseDTOS = accounts.stream().map(account -> this.modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());
        responseDTOS.removeIf(dto -> dto.getIban().equals(IBAN_BANK));
        return new ResponseEntity<List<AccountResponseDTO>>(responseDTOS, HttpStatus.OK);
    }





    // ** Get all accounts for a user
    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsByUserId(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) throws AccountNotFoundException {
        List<Account> accounts = accountService.getAllByUserId(userid);
        if (!canUserPerform(accounts.get(0).getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for these accounts!");
        }

        List<AccountResponseDTO> responseDTOS = accounts.stream().map(account -> this.modelMapper.map(account, AccountResponseDTO.class))
                .collect(Collectors.toList());
        responseDTOS.removeIf(dto -> !dto.isActivated());
        return new ResponseEntity<List<AccountResponseDTO>>(responseDTOS, HttpStatus.OK);
    }






    // ** Set new account limit
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountAbsoluteLimitResponseDTO> setAccountLimit(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the Absolute Limit of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountAbsoluteLimitDTO body) throws AccountNotFoundException, InvalidIbanException {
        if (body.getAbsoluteLimit() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account limit is missing!");
        }
        accountService.updateLimit(iban, body.getAbsoluteLimit());
        AccountAbsoluteLimitResponseDTO responseDTO = this.modelMapper.map(body, AccountAbsoluteLimitResponseDTO.class);
        return new ResponseEntity<AccountAbsoluteLimitResponseDTO>(responseDTO, HttpStatus.OK);
    }






    // ** Set new account pin
    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<AccountPincodeResponseDTO> setAccountPin(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the pincode of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountPincodeDTO body) throws AccountNotFoundException, InvalidIbanException, InvalidPincodeException {
        if (body.getOldPincode() == null || body.getNewPincode() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Old pincode and/or new pincode are missing!");
        }

        Account account = accountService.getOneByIban(iban);
        if (!canUserPerform(account.getUser().getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this action!");
        }

        accountService.updatePin(account, body.getOldPincode(), body.getNewPincode());
        AccountPincodeResponseDTO responseDTO = this.modelMapper.map(body, AccountPincodeResponseDTO.class);
        return new ResponseEntity<AccountPincodeResponseDTO>(responseDTO, HttpStatus.OK);
    }







    // ** Set new account status
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<AccountActivationResponseDTO> setAccountStatus(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountActivationDTO body) throws AccountNotFoundException, InvalidIbanException {
        if (body.isActivated() == null) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Account status is missing!");
        }
        accountService.updateStatus(iban, body.isActivated());
        AccountActivationResponseDTO responseDTO = this.modelMapper.map(body, AccountActivationResponseDTO.class);
        return new ResponseEntity<AccountActivationResponseDTO>(responseDTO, HttpStatus.OK);
    }






    // ** Authenticate account
    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<PinAuthenticateResponseDTO> authenticateAcount(PinAuthenticateDTO body) throws AccountNotFoundException, InvalidIbanException, InvalidPincodeException {
        PinAuthenticateResponseDTO responseDTO = this.modelMapper.map(body, PinAuthenticateResponseDTO.class);
        if (accountService.authenticateAccount(body.getIban(), body.getPincode())) {
            responseDTO.setIsValid(true);
            return new ResponseEntity<PinAuthenticateResponseDTO>(responseDTO, HttpStatus.OK);
        } else {
            throw new InvalidPincodeException("This pin was incorrect");
        }
    }






    // **** HELPER METHODS
    private boolean canUserPerform(String email) {
        String token = getValidatedToken();
        if (!jwtTokenProvider.getUsername(token).equals(email) && !jwtTokenProvider.getAuthentication(token).getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not authorized for this endpoint!");
        }

        User user = userService.findByEmail(jwtTokenProvider.getUsername(token));
        if (!user.getActivated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
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
