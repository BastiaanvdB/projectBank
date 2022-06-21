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

    private String checkAndReturnToken() throws InvalidAuthenticationException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidAuthenticationException("Token invalid or expired");
        }
        return token;
    }

    private User getOwnerOfToken(String token) throws InvalidAuthenticationException {
        User user = userService.findByEmail(jwtTokenProvider.getUsername(token));

        if (!user.getActivated()) {
            throw new InvalidAuthenticationException("Token invalid or expired");
        }

        return user;
    }


    public ResponseEntity<UserResponseDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserCreateDTO body) throws InvalidEmailException, PasswordRequirementsException {
        User user = this.modelMapper.map(body, User.class);

        user = userService.signup(user);
        UserResponseDTO response = this.modelMapper.map(user, UserResponseDTO.class);
        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "activated", required = false) Boolean activated, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "hasaccount", required = false) Boolean hasAccount) throws InvalidAuthenticationException {
        //check user his token
        getOwnerOfToken(checkAndReturnToken());

        if (offset == null) {
            offset = 0;
        }

        if (limit == null) {
            limit = 10;
        }

        boolean activatedFilterEnable = false;
        boolean accountFilterEnable = false;

        if (activated != null) {
            activatedFilterEnable = true;
        } else {
            activated = false;
        }

        if (hasAccount != null) {
            accountFilterEnable = true;
        } else {
            hasAccount = false;
        }

        UserFilterDTO userFilterDTO = new UserFilterDTO(offset, limit, firstname, lastname, accountFilterEnable, activatedFilterEnable, hasAccount, activated);

        // Get all users from service
        List<User> usersFiltered = userService.getAllWithFilter(userFilterDTO);

        // use mapper to map all users to user response data transfer object
        List<UserResponseDTO> responseDTOS = usersFiltered.stream().map(user -> this.modelMapper.map(user, UserResponseDTO.class)).collect(Collectors.toList());

        // return all response dto's and http 200
        return new ResponseEntity<List<UserResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getOneUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) throws UserNotFoundException, InvalidAuthenticationException, UnauthorizedException {

        // gets user throughs jwt that makes the request
        User user = getOwnerOfToken(checkAndReturnToken());

        // check if user request himself or not
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            throw new UnauthorizedException("Not allowed to get user!");
        }

        User requestedUser = userService.getOne(userid);

        UserResponseDTO response = this.modelMapper.map(requestedUser, UserResponseDTO.class);
        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserPassword(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the password of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body) throws UserNotFoundException, PasswordRequirementsException, InvalidAuthenticationException, UnauthorizedException, InvalidOldPasswordException {


        // gets user throughs jwt that makes the request
        User user = getOwnerOfToken(checkAndReturnToken());

        //check if user want to change himself
        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            throw new UnauthorizedException("Not the authority to change password for user!");
        }
        userService.changePassword(userid, user, body);
        return new ResponseEntity("Password successfully changed!", HttpStatus.ACCEPTED);
    }


    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserRole(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the role of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserRoleDTO body) throws UserNotFoundException, InvalidRoleException, InvalidAuthenticationException {

        //check user his token
        getOwnerOfToken(checkAndReturnToken());

        userService.changeUserRoles(userid, body);
        return new ResponseEntity("Role successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserStatus(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserActivationDTO body) throws UserNotFoundException, InvalidAuthenticationException {

        //check user his token
        getOwnerOfToken(checkAndReturnToken());

        userService.changeUserStatus(userid, body);

        return new ResponseEntity("Activation successfully changed!", HttpStatus.ACCEPTED);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<InlineResponse200> updateUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Update an existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserDTO body) throws UserNotFoundException, InvalidEmailException, InvalidAuthenticationException, UnauthorizedException {

        boolean adminforce = false;
        User newUserDetails = this.modelMapper.map(body, User.class);


        // gets user throughs jwt that makes the request
        User user = getOwnerOfToken(checkAndReturnToken());

        if (userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            throw new UnauthorizedException("Not the authority to update userdetails for requested user");
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

    public ResponseEntity<InlineResponse200> usersLoginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UsersLoginBody body) throws InvalidAuthenticationException {
        String token = null;

        // Get token with login credentials
        token = userService.login(body);

        // Create response body and set token
        InlineResponse200 res = new InlineResponse200();
        res.setToken(token);

        // Return with http status 200
        return new ResponseEntity<InlineResponse200>(res, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') || hasRole('EMPLOYEE')")
    public ResponseEntity<UserResponseDTO> getCurrentUser() throws InvalidAuthenticationException {
        // gets user throughs jwt that makes the request
        User user = getOwnerOfToken(checkAndReturnToken());

        UserResponseDTO response = this.modelMapper.map(user, UserResponseDTO.class);

        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.OK);
    }
}
