package io.swagger.api;

import io.swagger.annotations.Api;
import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.ResponseDTO.InlineResponse200;
import io.swagger.model.ResponseDTO.UserResponseDTO;
import io.swagger.model.UsersLoginBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.security.JwtTokenProvider;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.*;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@RestController
@Api(tags = "Users")
public class UsersApiController implements UsersApi {

    private static final Logger log = LoggerFactory.getLogger(UsersApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public UsersApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<UserResponseDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserCreateDTO body) {

        ModelMapper modelMapper = new ModelMapper();
        User user = modelMapper.map(body, User.class);
        user = userService.signup(user);
        UserResponseDTO response = modelMapper.map(user, UserResponseDTO.class);

        return new ResponseEntity<UserResponseDTO>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<List<AccountResponseDTO>> getAllAccountsByUserId(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<AccountResponseDTO>>(objectMapper.readValue("[ {\n  \"pin\" : 1234,\n  \"balance\" : 0.8008281904610115,\n  \"user_Id\" : 1,\n  \"absolute_Limit\" : 10,\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"employee_Id\" : 2,\n  \"type\" : \"Current\",\n  \"activated\" : true\n}, {\n  \"pin\" : 1234,\n  \"balance\" : 0.8008281904610115,\n  \"user_Id\" : 1,\n  \"absolute_Limit\" : 10,\n  \"iban\" : \"NLxxINHO0xxxxxxxxx\",\n  \"employee_Id\" : 2,\n  \"type\" : \"Current\",\n  \"activated\" : true\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<AccountResponseDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<AccountResponseDTO>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status) {

        // Get all users from service, create model mapper
        List<User> users = userService.getAll();
        ModelMapper modelMapper = new ModelMapper();

        // use mapper to map all users to user response data transfer object
        List<UserResponseDTO> responseDTOS = users.stream().map(user -> modelMapper.map(user, UserResponseDTO.class))
                .collect(Collectors.toList());

        // return all response dto's and http 200
        return new ResponseEntity<List<UserResponseDTO>>(responseDTOS, HttpStatus.OK);
    }

    public ResponseEntity<UserResponseDTO> getOneUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid) {

        //nog doen


        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<UserResponseDTO>(objectMapper.readValue("{\n  \"firstname\" : \"Henk\",\n  \"address\" : \"Bijdorplaan 15\",\n  \"role\" : 0,\n  \"city\" : \"Haarlem\",\n  \"phone\" : \"0623445321\",\n  \"transaction_Limit\" : 2000,\n  \"postalCode\" : \"2015CE\",\n  \"day_limit\" : 5000,\n  \"id\" : 1,\n  \"email\" : \"henkbakker@test.nl\",\n  \"lastname\" : \"Bakker\",\n  \"activated\" : true\n}", UserResponseDTO.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<UserResponseDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<UserResponseDTO>(HttpStatus.NOT_IMPLEMENTED);
    }

//    @PreAuthorize("hasRole('EMPLOYEE', 'USER')")
    public ResponseEntity<Void> setUserPassword(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the password of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserPasswordDTO body) {

        boolean force = false;

        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        String userEmail = jwtTokenProvider.getUsername(token);

        // gets user throughs jwt that makes the request
        User user = userService.getUserOnEmail(userEmail);

        if(userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not the authority to change password for user");
        }

        if (body.getNewPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || body.getNewPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New password doesnt meet security requirements!");
        }

        if(user.getRoles().contains(Role.ROLE_EMPLOYEE))
        {
            user = userService.getOne(userid);
            force = true;
        }
        userService.changePassword(user, body.getNewPassword(), body.getOldPassword(), force);

        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

//    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserRole(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the role of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserRoleDTO body) {

        // checks if atleast one rola has been given
        if(body.getRoles().size() == 0 || body.getRoles() == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No role provided for user!");
        }

        List<Role> roles = new ArrayList<>();
        for (Integer r: body.getRoles())
        {
            roles.add(Role.values()[r]);
        }

        User user = userService.getOne(userid);

        //checks if user by userid exists
        if(user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No user found with provided userid!");
        }

        user.setRoles(roles);
        userService.add(user);

        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    //    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Void> setUserStatus(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserActivationDTO body) {


        User user = userService.getOne(userid);

        //checks if user by userid exists
        if(user == null)
        {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "No user found with provided userid!");
        }

        user.setActivated(body.isActivated());

        userService.add(user);

        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<Void> updateUser(@Min(1) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema(allowableValues = {}, minimum = "1"
    )) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Update an existing user with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody UserDTO body) {

        ModelMapper modelMapper = new ModelMapper();
        User newUserDetails = modelMapper.map(body, User.class);


        String token = jwtTokenProvider.resolveToken(request);
        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        String userEmail = jwtTokenProvider.getUsername(token);

        // gets user throughs jwt that makes the request
        User user = userService.getUserOnEmail(userEmail);

        if(userid != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not the authority to update userdetails for requested user");
        }

        if(user.getRoles().contains(Role.ROLE_EMPLOYEE))
        {
            user = userService.getOne(userid);
        }

        if (newUserDetails.getFirstname().length() < 2 || newUserDetails.getLastname().length() < 2 || newUserDetails.getPhone().length() < 10 || newUserDetails.getPostalCode().length() < 6 || newUserDetails.getCity().length() < 2 || newUserDetails.getAddress().length() < 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enter all user details!");
        }

        if (!EmailValidator.getInstance().isValid(newUserDetails.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a correct email!");
        }


        System.out.println(newUserDetails.getEmail());
        System.out.println(user.getEmail());

        if ((userService.findByEmail(newUserDetails.getEmail()) != null && (newUserDetails.getEmail() != user.getEmail())) || (newUserDetails.getEmail() == user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email already has been used!");
        }

        user.setFirstname(newUserDetails.getFirstname());
        user.setLastname(newUserDetails.getLastname());
        user.setEmail(newUserDetails.getEmail());
        user.setPhone(newUserDetails.getPhone());
        user.setAddress(newUserDetails.getAddress());
        user.setPostalCode(newUserDetails.getPostalCode());
        user.setCity(newUserDetails.getCity());

        userService.add(user);

        return new ResponseEntity<Void>(HttpStatus.ACCEPTED);
    }

    public ResponseEntity<InlineResponse200> usersLoginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema = @Schema()) @Valid @RequestBody UsersLoginBody body) {

        // Get token with data from POST body
        String token = userService.login(body.getEmail(), body.getPassword());

        // Create response body and set token
        // **** @Bastiaan weet jij wat dit inlineResponse200 is? Het overerft van AuthorizationResponse ****
        // **** @Bastiaan waar staat dat id voor in die AuthorizationResponse? ****
        InlineResponse200 res = new InlineResponse200();
        res.setToken(token);

        // Return ..... with http status 200
        return new ResponseEntity<InlineResponse200>(res, HttpStatus.OK);
    }
}
