/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.34).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import io.swagger.model.DTO.*;
import io.swagger.model.ResponseDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")
@Validated
public interface AccountsApi {

    @Operation(summary = "Creating a new account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Newly created account with iban NLxx ABNAxxxxxxxxxxx"),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint")})
    @RequestMapping(value = "/accounts",
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<AccountResponseDTO> createAccount(@Parameter(in = ParameterIn.DEFAULT, description = "Post a new account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountDTO body);


    @Operation(summary = "Do a deposit on account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The newly made deposit", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DepositResponseDTO.class)))),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint")})
    @RequestMapping(value = "/accounts/{IBAN}/deposit",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<DepositResponseDTO> createDeposit(@Size(min = 18, max = 18) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a deposit to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody DepositDTO body);


    @Operation(summary = "Do a withdraw of account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "The newly made withdraw", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = WithdrawResponseDTO.class)))),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint")})
    @RequestMapping(value = "/accounts/{IBAN}/withdraw",
            produces = {"application/json"},
            consumes = {"application/json"},
            method = RequestMethod.POST)
    ResponseEntity<WithdrawResponseDTO> createWithdraw(@Size(min = 18, max = 18) @Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("IBAN") String IBAN, @Parameter(in = ParameterIn.DEFAULT, description = "Post a withdraw to this endpoint", required = true, schema = @Schema()) @Valid @RequestBody WithdrawDTO body);


    @Operation(summary = "Get one specific account", description = "Get one account with a specific iban given as parameter", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "One user account", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "Account with this iban could not be found")})
    @RequestMapping(value = "/accounts/{iban}",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<AccountResponseDTO> getAccountByIban(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban);


    @Operation(summary = "Get all available accounts", description = "This endpoint will provide all available accounts when logged as an employee, otherwise it will return only the logged customer data.", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All user accounts", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AccountResponseDTO.class)))),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "Accounts could not be found")})
    @RequestMapping(value = "/accounts",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<AccountResponseDTO>> getAllAccounts(@Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "offset", required = false) Integer offset, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "limit", required = false) Integer limit, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "firstname", required = false) String firstname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "lastname", required = false) String lastname, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "status", required = false) String status);


    @Operation(summary = "Get all from account", description = "Get all the transactions from a account with given parameter", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Transactions"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All transactions", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDTO.class)))),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "Account with this iban could not be found")})
    @RequestMapping(value = "/accounts/{iban}/transactions",
            produces = {"application/json"},
            method = RequestMethod.GET)
    ResponseEntity<List<TransactionResponseDTO>> getAllTransactionsFromAccount(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "IBAN To", required = false) String ibANTo, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "balance operator", required = false) String balanceOperator, @Parameter(in = ParameterIn.QUERY, description = "", schema = @Schema()) @Valid @RequestParam(value = "Balance", required = false) String balance);


    @Operation(summary = "Update Absolute Limit of specific account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Absolute Limit is updated"),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "account not found with given iban")})
    @RequestMapping(value = "/accounts/{iban}",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<AccountAbsoluteLimitResponseDTO> setAccountLimit(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the Absolute Limit of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountAbsoluteLimitDTO body);


    @Operation(summary = "Update pincode of specific account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "account pincode is updated"),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "account not found with given iban")})
    @RequestMapping(value = "/accounts/{iban}/pincode",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<AccountPincodeResponseDTO> setAccountPin(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the pincode of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountPincodeDTO body);


    @Operation(summary = "Update activation of specific account", description = "", security = {
            @SecurityRequirement(name = "bearerAuth")}, tags = {"Accounts"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account activation is updated"),

            @ApiResponse(responseCode = "403", description = "Not authorized for this endpoint"),

            @ApiResponse(responseCode = "404", description = "Account not found with given iban")})
    @RequestMapping(value = "/accounts/{iban}/activation",
            consumes = {"application/json"},
            method = RequestMethod.PUT)
    ResponseEntity<AccountActivationResponseDTO> setAccountStatus(@Parameter(in = ParameterIn.PATH, description = "", required = true, schema = @Schema()) @PathVariable("iban") String iban, @Parameter(in = ParameterIn.DEFAULT, description = "Change the activation of a existing account with this endpoint", required = true, schema = @Schema()) @Valid @RequestBody AccountActivationDTO body);

}

