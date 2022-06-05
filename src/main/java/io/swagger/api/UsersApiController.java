package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.InlineResponse200;
import io.swagger.model.ResponseDTO.UserResponseDTO;
import io.swagger.model.UsersLoginBody;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.apache.commons.validator.routines.EmailValidator;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }


    private User checkTokenAndReturnUser() {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
        }

        User user = userService.findByEmail(jwtTokenProvider.getUsername(token));

        if (!user.getActivated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalid or expired");
        }

        return user;
    }


    public ResponseEntity<UserResponseDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserCreateDTO body) {

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(body, User.class);


        if (user.getFirstname().length() < 2 || user.getLastname().length() < 2 || user.getPhone().length() < 10 || user.getPostalCode().length() < 6 || user.getCity().length() < 2 || user.getAddress().length() < 2) {
            return new ResponseEntity("Enter all user details!", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            return new ResponseEntity("Enter a correct email!", HttpStatus.NOT_ACCEPTABLE);
        }

        if (userService.findByEmail(user.getEmail()) != null) {
            return new ResponseEntity("Email already has been used!", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || user.getPassword().length() < 6) {
            return new ResponseEntity("Password doesnt meet security requirements!", HttpStatus.NOT_ACCEPTABLE);
        }

        try {

            user = userService.signup(user);

        } catch (Exception ex) {
            return new ResponseEntity("Enter all user details!", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);

        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "activated", required = false) Boolean activated, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "hasaccount", required = false) Boolean hasAccount) {

        if (offset == null) {
            offset = 0;
        }

        if (limit == null) {
            limit = 10;
        }

        //check user his token
        checkTokenAndReturnUser();

        // Get all users from service, create model mapper
        List<User> users = userService.getAll(offset, limit);

        ModelMapper modelMapper = new ModelMapper();
        List<User> usersFilteredNames = new ArrayList<>();
        List<User> usersFilteredStatus = new ArrayList<>();
        List<User> userFilteredHasAccount = new ArrayList<>();

        // filters users on firstname, lastname or both
        for (User s : users) {
        if(s.getEmail() != "bank@live.nl") {
            if (firstname != null && lastname == null) {
                if (s.getFirstname().toLowerCase().contains(firstname.toLowerCase())) {
                    usersFilteredNames.add(s);
                }
            }

            if (firstname == null && lastname != null) {
                if (s.getLastname().toLowerCase().contains(lastname.toLowerCase())) {
                    usersFilteredNames.add(s);
                }
            }

            if (firstname != null && lastname != null) {

                if (s.getFirstname().toLowerCase().contains(firstname.toLowerCase()) && s.getLastname().toLowerCase().contains(lastname.toLowerCase())) {
                    usersFilteredNames.add(s);
                }
            }

            if (firstname == null && lastname == null) {
                usersFilteredNames.add(s);
            }
        }
        }


        // filters users on activation
        if (activated != null) {
            for (User s : usersFilteredNames) {
                if (activated) {

                    if (s.getActivated()) {
                        usersFilteredStatus.add(s);
                    }
                } else {
                    if (!s.getActivated()) {
                        usersFilteredStatus.add(s);
                    }
                }
            }
        } else {
            usersFilteredStatus = usersFilteredNames;
        }

        // filters users on owning a account or not
        if (hasAccount != null) {
            for (User s : usersFilteredStatus) {
                if (hasAccount) {
                    if (accountService.getAllByUserId(s.getId()) != null) {
                        userFilteredHasAccount.add(s);
                    }
                } else {
                    if (accountService.getAllByUserId(s.getId()) == null) {
                        userFilteredHasAccount.add(s);
                    }
                }
            }
        } else {
            userFilteredHasAccount = usersFilteredStatus;
        }

        // use mapper to map all users to user response data transfer object
        List<UserResponseDTO> responseDTOS = userFilteredHasAccount.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());


        // return all response dto's and http 200
        return new ResponseEntity<List<UserResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getOneUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) {

        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        // check if user request himself or not
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not allowed to get user!", HttpStatus.NOT_ACCEPTABLE);
        }

        User requestedUser = userService.getOne(userid);

        if (requestedUser == null) {
            return new ResponseEntity("No user found with provided userid!", HttpStatus.NOT_ACCEPTABLE);
        }

        ModelMapper modelMapper = new ModelMapper();
        UserResponseDTO response = modelMapper.map(requestedUser, UserResponseDTO.class);

        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserPassword(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the password of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body) {

        boolean force = false;

        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        //check if user want to change himself
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not the authority to change password for user", HttpStatus.NOT_ACCEPTABLE);
        }

        if (body.getNewPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || body.getNewPassword().length() < 6) {
            return new ResponseEntity("New password doesnt meet security requirements!", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE) && user.getId() != userid) {
            user = userService.getOne(userid);
            force = true;
        }

        try {
            userService.changePassword(user, body.getNewPassword(), body.getOldPassword(), force);
        } catch (AuthenticationException ex) {
            return new ResponseEntity("Current password is invalid!", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return new ResponseEntity("Password successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserRole(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the role of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserRoleDTO body) {

        //check user his token
        checkTokenAndReturnUser();

        // checks if atleast one rola has been given
        if (body.getRoles().size() == 0 || body.getRoles() == null) {
            return new ResponseEntity("No role provided for user!", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Role> roles = new ArrayList<>();
        for (Integer r : body.getRoles()) {
            roles.add(Role.values()[r]);
        }

        User user = userService.getOne(userid);

        //checks if user by userid exists
        if (user == null) {
            return new ResponseEntity("No user found with provided userid!", HttpStatus.NOT_ACCEPTABLE);
        }

        user.setRoles(roles);
        userService.put(user);

        return new ResponseEntity("Role successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserStatus(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserActivationDTO body) {

        //check user his token
        checkTokenAndReturnUser();

        User user = userService.getOne(userid);

        //checks if user by userid exists
        if (user == null) {
            return new ResponseEntity("No user found with provided userid!", HttpStatus.NOT_ACCEPTABLE);
        }

        user.setActivated(body.isActivated());

        userService.put(user);

        return new ResponseEntity("Activation successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<InlineResponse200> updateUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Update an existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserDTO body) {

        boolean admin = false;
        ModelMapper modelMapper = new ModelMapper();
        User newUserDetails = modelMapper.map(body, User.class);


        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not the authority to update userdetails for requested user", HttpStatus.NOT_ACCEPTABLE);
        }

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            admin = true;
            user = userService.getOne(userid);
        }

        if (newUserDetails.getFirstname().length() < 2 || newUserDetails.getLastname().length() < 2 || newUserDetails.getPhone().length() < 10 || newUserDetails.getPostalCode().length() < 6 || newUserDetails.getCity().length() < 2 || newUserDetails.getAddress().length() < 2) {
            return new ResponseEntity("Enter all user details!", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!EmailValidator.getInstance().isValid(newUserDetails.getEmail())) {
            return new ResponseEntity("Enter a correct email!", HttpStatus.NOT_ACCEPTABLE);
        }


        //check if new email already has been taken
        User checkUser = userService.findByEmail(newUserDetails.getEmail());

        //check if its not the same user
        if (checkUser != null) {
            if (checkUser.getId() != user.getId()) {
                return new ResponseEntity("Email already has been used!", HttpStatus.NOT_ACCEPTABLE);
            }
        }

        user.setFirstname(newUserDetails.getFirstname());
        user.setLastname(newUserDetails.getLastname());
        user.setEmail(newUserDetails.getEmail());
        user.setPhone(newUserDetails.getPhone());
        user.setAddress(newUserDetails.getAddress());
        user.setPostalCode(newUserDetails.getPostalCode());
        user.setCity(newUserDetails.getCity());
        user.setTransactionLimit(newUserDetails.getTransactionLimit());
        user.setDayLimit(newUserDetails.getDayLimit());

        String token = userService.EditUserAndToken(user);

        if (!admin) {
            InlineResponse200 res = new InlineResponse200();
            res.setToken(token);
            // Return new token with http status 200
            return new ResponseEntity<InlineResponse200>(res, HttpStatus.OK);
        } else {
            return new ResponseEntity("User has been updated!", HttpStatus.OK);
        }
    }

    public ResponseEntity<InlineResponse200> usersLoginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UsersLoginBody body) {
        String token = null;

        // Get token with data from POST body

        try {
            token = userService.login(body.getEmail(), body.getPassword());
        } catch (AuthenticationException ex) {
            return new ResponseEntity("Invalid user credentials.", HttpStatus.UNAUTHORIZED);
        }

        if (token == null) {
            return new ResponseEntity("Invalid user credentials.", HttpStatus.UNAUTHORIZED);
        }
        // Create response body and set token
        InlineResponse200 res = new InlineResponse200();
        res.setToken(token);

        // Return with http status 200
        return new ResponseEntity<InlineResponse200>(res, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> usersCurrentGet() {


        ModelMapper modelMapper = new ModelMapper();

        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        if (user == null) {
            return new ResponseEntity("Token invalid or expired", HttpStatus.UNAUTHORIZED);
        }

        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);


        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.OK);
    }
}
