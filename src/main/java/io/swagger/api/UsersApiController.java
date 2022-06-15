package io.swagger.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.InlineResponse200;
import io.swagger.model.ResponseDTO.UserResponseDTO;
import io.swagger.model.UsersLoginBody;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.*;
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
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;

    @Autowired
    private UserService userService;


    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
        this.modelMapper = new ModelMapper();
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


    public ResponseEntity<UserResponseDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserCreateDTO body) throws InvalidEmailException, PasswordRequirementsException {

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(body, User.class);

        user = userService.signup(user);
        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);
        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "activated", required = false) Boolean activated, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "hasaccount", required = false) Boolean hasAccount) throws AccountNotFoundException {
        ModelMapper modelMapper = new ModelMapper();

        //check user his token
        checkTokenAndReturnUser();

        if (offset == null) {
            offset = 0;
        }

        if (limit == null) {
            limit = 10;
        }

        UserFilterDTO userFilterDTO = new UserFilterDTO();
        userFilterDTO.setLimit(limit);
        userFilterDTO.setOffset(offset);
        userFilterDTO.setFirstname(firstname);
        userFilterDTO.setLastname(lastname);

        if(activated != null)
        {
            userFilterDTO.setActivatedFilterEnable(true);
            userFilterDTO.setActivated(activated);
        }

        if(hasAccount != null)
        {
            userFilterDTO.setAccountFilterEnable(true);
            userFilterDTO.setHasAccount(hasAccount);
        }



        List<User> usersFiltered = userService.getAllWithFilter(userFilterDTO);

//
//        // Get all users from service, create model mapper
//
//        List<User> usersFilteredNames = new ArrayList<>();
//        List<User> usersFilteredStatus = new ArrayList<>();
//        List<User> userFilteredHasAccount = new ArrayList<>();
//
//        // filters users on firstname, lastname or both
//        for (User s : users) {
//            if (s.getEmail() != "bank@bbcbank.nl") {
//                if (firstname != null && lastname == null) {
//                    if (s.getFirstname().toLowerCase().contains(firstname.toLowerCase())) {
//                        usersFilteredNames.add(s);
//                    }
//                }
//
//                if (firstname == null && lastname != null) {
//                    if (s.getLastname().toLowerCase().contains(lastname.toLowerCase())) {
//                        usersFilteredNames.add(s);
//                    }
//                }
//
//                if (firstname != null && lastname != null) {
//
//                    if (s.getFirstname().toLowerCase().contains(firstname.toLowerCase()) && s.getLastname().toLowerCase().contains(lastname.toLowerCase())) {
//                        usersFilteredNames.add(s);
//                    }
//                }
//
//                if (firstname == null && lastname == null) {
//                    usersFilteredNames.add(s);
//                }
//            }
//        }
//
//
//        // filters users on activation
//        if (activated != null) {
//            for (User s : usersFilteredNames) {
//                if (activated) {
//
//                    if (s.getActivated()) {
//                        usersFilteredStatus.add(s);
//                    }
//                } else {
//                    if (!s.getActivated()) {
//                        usersFilteredStatus.add(s);
//                    }
//                }
//            }
//        } else {
//            usersFilteredStatus = usersFilteredNames;
//        }
//
//        // filters users on owning a account or not
//        if (hasAccount != null) {
//            for (User s : usersFilteredStatus) {
//                if (hasAccount) {
//                    if (accountService.getAllByUserId(s.getId()).size() > 0 && accountService.getAllByUserId(s.getId()) != null) {
//                        userFilteredHasAccount.add(s);
//                    }
//                } else {
//                    if (accountService.getAllByUserId(s.getId()).size() == 0 && accountService.getAllByUserId(s.getId()) != null) {
//                        userFilteredHasAccount.add(s);
//                    }
//                }
//            }
//        } else {
//            userFilteredHasAccount = usersFilteredStatus;
//        }

        // use mapper to map all users to user response data transfer object
        List<UserResponseDTO> responseDTOS = usersFiltered.stream().map(user -> modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());


        // return all response dto's and http 200
        return new ResponseEntity<List<UserResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getOneUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) throws UserNotFoundException {

        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        // check if user request himself or not
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not allowed to get user!", HttpStatus.UNAUTHORIZED);
        }

        User requestedUser = userService.getOne(userid);

        ModelMapper modelMapper = new ModelMapper();
        UserResponseDTO response = modelMapper.map(requestedUser, UserResponseDTO.class);

        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserPassword(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the password of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body) throws UserNotFoundException, PasswordRequirementsException {


        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        //check if user want to change himself
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not the authority to change password for user", HttpStatus.UNAUTHORIZED);
        }
        userService.changePassword(userid, user, body);
        return new ResponseEntity("Password successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserRole(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the role of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserRoleDTO body) throws UserNotFoundException, InvalidRoleException {

        //check user his token
        checkTokenAndReturnUser();

        userService.changeUserRoles(userid, body);
        return new ResponseEntity("Role successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserStatus(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserActivationDTO body) throws UserNotFoundException {

        //check user his token
        checkTokenAndReturnUser();

        userService.changeUserStatus(userid, body);

        return new ResponseEntity("Activation successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<InlineResponse200> updateUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Update an existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserDTO body) throws UserNotFoundException, InvalidEmailException {

        boolean adminforce = false;
        ModelMapper modelMapper = new ModelMapper();
        User newUserDetails = modelMapper.map(body, User.class);


        // gets user throughs jwt that makes the request
        User user = this.checkTokenAndReturnUser();

        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            return new ResponseEntity("Not the authority to update userdetails for requested user", HttpStatus.UNAUTHORIZED);
        }

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE) && userid != user.getId()) {
            adminforce = true;
        }

        String token = userService.EditUserAndToken(userid, user, body);

        if (!adminforce) {
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
