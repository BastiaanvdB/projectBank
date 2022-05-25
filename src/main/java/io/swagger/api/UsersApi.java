/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.ResponseDTO.InlineResponse200;
import io.swagger.model.ResponseDTO.UserResponseDTO;
import io.swagger.model.UsersLoginBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@Validated
public interface UsersApi {

    @Operation(summary = "Creating a new user", description = "", tags={ "Users" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User has been created.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),

//            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint")
    })
    @RequestMapping(value = "/users",
            produces = { "application/json" },
            consumes = { "application/json" },
            method = RequestMethod.POST)
    ResponseEntity<UserResponseDTO> createUser(@Parameter(in = ParameterIn.DEFAULT, description = "Create a new user with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody UserCreateDTO body);

    @Operation(summary = "Get all accounts of specific user", description = "Get all the accounts of the user with the id given as parameter", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "All accounts of user", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountResponseDTO.class)))),
        
        @ApiResponse(responseCode = "400", description = "User with this id could not be found"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint") })
    @RequestMapping(value = "/users/{userid}/accounts",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<AccountResponseDTO>> getAllAccountsByUserId(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid);


    @Operation(summary = "Get all users", description = "This endpoint will provide all available users", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "All users", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),
        
        @ApiResponse(responseCode = "404", description = "No user could could be found") })
    @RequestMapping(value = "/users",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<List<UserResponseDTO>> getAllUsers(@Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "" ,schema=@Schema()) @Valid @RequestParam(value = "status", required = false) String status);


    @Operation(summary = "Get one specific user", description = "Get one user with the given id as parameter", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "One user", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))),
        
        @ApiResponse(responseCode = "400", description = "User with this id could not be found"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint") })
    @RequestMapping(value = "/users/{userid}",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<UserResponseDTO> getOneUser(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid);


    @Operation(summary = "Change password of specific user", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User password is updated"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),
        
        @ApiResponse(responseCode = "404", description = "User not found with given id") })
    @RequestMapping(value = "/users/{userid}/password",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> setUserPassword(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the password of a existing user with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody UserPasswordDTO body);


    @Operation(summary = "Change role of specific user", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User role is updated"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),
        
        @ApiResponse(responseCode = "404", description = "User not found with given id") })
    @RequestMapping(value = "/users/{userid}/role",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> setUserRole(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the role of a existing user with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody UserRoleDTO body);


    @Operation(summary = "Change activation status of specific user", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User activation is updated"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),
        
        @ApiResponse(responseCode = "404", description = "User not found with given id") })
    @RequestMapping(value = "/users/{userid}/activation",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<Void> setUserStatus(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing user with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody UserActivationDTO body);


    @Operation(summary = "Update a specific user", description = "", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={ "Users" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "User is updated"),
        
        @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),
        
        @ApiResponse(responseCode = "404", description = "User not found with given id") })
    @RequestMapping(value = "/users/{userid}",
        consumes = { "application/json" }, 
        method = RequestMethod.PUT)
    ResponseEntity<InlineResponse200> updateUser(@Min(1)@Parameter(in = ParameterIn.PATH, description = "", required=true, schema=@Schema(allowableValues={  }, minimum="1"
)) @PathVariable("userid") Integer userid, @Parameter(in = ParameterIn.DEFAULT, description = "Update an existing user with this endpoint", required=true, schema=@Schema()) @Valid @RequestBody UserDTO body);


    @Operation(summary = "Authenticate user", description = "This call returns a JWT token.", tags={ "Authorization" })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Authentication OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InlineResponse200.class))),
        
        @ApiResponse(responseCode = "401", description = "Authentication failed") })
    @RequestMapping(value = "/users/login",
        produces = { "application/json" }, 
        consumes = { "application/json" }, 
        method = RequestMethod.POST)
    ResponseEntity<InlineResponse200> usersLoginPost(@Parameter(in = ParameterIn.DEFAULT, description = "", schema=@Schema()) @Valid @RequestBody UsersLoginBody body);

}

