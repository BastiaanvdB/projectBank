package io.swagger.steps.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.swagger.model.UsersLoginBody;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.steps.BaseStepDefinitions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class UserStepDefinitions extends BaseStepDefinitions {

    private final String validTokenEmployee = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJrQGJiY2JhbmsubmwiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX0VNUExPWUVFIn0seyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTMwNzA1NywiZXhwIjoxNjU2MTcxMDU3fQ.LsJXusVw8ic2nrn_My8pQ8X-iTgEF_pLu2yawquDMxU";
    private final String validTokenUser = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicmFtQGxpdmUubmwiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTMwNzA5MCwiZXhwIjoxNjU2MTcxMDkwfQ.RLQ19uw6HYVY0JE99V_jDkLVtwsFnZw7DR54EMgqfBI";
    private final ObjectMapper mapper = new ObjectMapper();

    private ResponseEntity<String> callGetHttpHeaders(String token, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        return new TestRestTemplate().exchange(
                getBaseUrl() + url, HttpMethod.GET, new HttpEntity<>(httpHeaders),
                String.class);
    }

    private ResponseEntity<String> callPostHttpHeaders(String token, String url, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);

        HttpEntity<String> request = new HttpEntity<String>(body, httpHeaders);
        return new TestRestTemplate().postForEntity(getBaseUrl() + url,
                request, String.class);
    }

    private ResponseEntity<String> callPutHttpHeaders(String token, String url, String body) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        return new TestRestTemplate().exchange(
                getBaseUrl() + url, HttpMethod.PUT, new HttpEntity<>(body, httpHeaders),
                String.class);
    }

    private ResponseEntity<String> callPostHttpHeadersWithoutJwt(UsersLoginBody body) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(
                body),
                httpHeaders);
        return new TestRestTemplate().postForEntity(getBaseUrl() + "/Groep1BankApi/bank/1.0.0/users/login",
                request, String.class);
    }

    private ResponseEntity<String> createResponse;
    @Given("I have valid jwt to create new user")
    public void iHaveValidJwtToCreateNewUser() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint to create new user")
    public void iCallEndpointToCreateNewUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("firstname", "bastiaan");
        body.put("lastname", "van der bijl");
        body.put("address", "Current");
        body.put("city", "Current");
        body.put("postalCode", "Current");
        body.put("email", "bas@live.nl");
        body.put("password", "BastiaanTest");
        body.put("phone", "0629735611");
        createResponse = callPostHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/signup", String.valueOf(body));
    }

    @Then("I receive http code {int} created new user")
    public void iReceiveHttpCodeCreatedNewUser(int status) {
        Assertions.assertEquals(status, createResponse.getStatusCodeValue());
    }

    @And("I get newly created user")
    public void iGetNewlyCreatedUser() throws JSONException {
        JSONObject obj = new JSONObject(createResponse.getBody());
        Assertions.assertTrue(obj.getString("firstname").startsWith("bas"));
    }


    private ResponseEntity<String> users;
    @Given("I have valid jwt to get all users")
    public void iHaveValidJwtToGetAllUsers() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint to get all users")
    public void iCallEndpointToGetAllUsers() {
        users = callGetHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users");
    }

    @Then("I receive http code {int} for get all users")
    public void iReceiveHttpCodeForGetAllUsers(int status) {
        Assertions.assertEquals(status, users.getStatusCodeValue());
    }

    @And("I get list with all users")
    public void iGetListWithAllUsers() throws JSONException {
        JSONArray obj = new JSONArray(users.getBody());
        String firstUser = obj.getString(0);
        Assertions.assertTrue(firstUser.contains("firstname"));
    }


    private ResponseEntity<String> userByUserId;
    @Given("I have valid jwt to get one user")
    public void iHaveValidJwtToGetOneUser() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint to get one user")
    public void iCallEndpointToGetOneUser() {
        userByUserId = callGetHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/2");
    }

    @Then("I receive http code {int} for get one user")
    public void iReceiveHttpCodeForGetOneUser(int status) {
        Assertions.assertEquals(status, userByUserId.getStatusCodeValue());
    }

    @And("I get one user")
    public void iGetOneUser() throws JSONException {
        JSONObject obj = new JSONObject(userByUserId.getBody());
        Assertions.assertTrue(obj.getString("firstname").startsWith("Bra"));
    }


    private ResponseEntity<String> userWithNewPassword;
    @Given("I have valid jwt to set the password of user")
    public void iHaveValidJwtToSetThePasswordOfUser() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint to set password of user")
    public void iCallEndpointToSetPasswordOfUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("oldPassword", "BramTest");
        body.put("newPassword", "BramTestNew");
        userWithNewPassword = callPutHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/users/2/password", String.valueOf(body));
    }

    @Then("I receive http code {int} for setting password")
    public void iReceiveHttpCodeForSettingPassword(int status) {
        Assertions.assertEquals(status, userWithNewPassword.getStatusCodeValue());
    }


    private ResponseEntity<String> userWithNewRole;
    @Given("I have valid jwt to set the role of user")
    public void iHaveValidJwtToSetTheRoleOfUser() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint to set role of user")
    public void iCallEndpointToSetRoleOfUser() throws JSONException {
        JSONObject body = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(0);
        arr.put(1);
        body.put("roles", arr);
        userWithNewRole = callPutHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/2/role", String.valueOf(body));
    }

    @Then("I receive http code {int} for setting role")
    public void iReceiveHttpCodeForSettingRole(int status) {
        Assertions.assertEquals(status, userWithNewRole.getStatusCodeValue());
    }



    private ResponseEntity<String> userWithNewStatus;
    @Given("I have valid jwt to set the status of user")
    public void iHaveValidJwtToSetTheStatusOfUser() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint to set status of user")
    public void iCallEndpointToSetStatusOfUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("activated", false);
        userWithNewStatus = callPutHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/2/activation", String.valueOf(body));
    }

    @Then("I receive http code {int} for setting status")
    public void iReceiveHttpCodeForSettingStatus(int status) {
        Assertions.assertEquals(status, userWithNewStatus.getStatusCodeValue());
    }



    private ResponseEntity<String> updatedUser;
    @Given("I have valid jwt to update user")
    public void iHaveValidJwtToUpdateUser() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint to update user")
    public void iCallEndpointToUpdateUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("firstname", "bastiaan");
        body.put("lastname", "van der bijl");
        body.put("address", "Driehuizerkerkweg 100");
        body.put("city", "Beverwijk");
        body.put("postalCode", "1985HC");
        body.put("email", "bastiaan@live.nl");
        body.put("phone", "0682557510");
        body.put("transaction_Limit", 999);
        body.put("day_limit", 10);
        updatedUser = callPutHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/2", String.valueOf(body));
    }

    @Then("I receive http code {int} for updating user")
    public void iReceiveHttpCodeForUpdatingUser(int status) {
        Assertions.assertEquals(status, updatedUser.getStatusCodeValue());
    }


    private UsersLoginBody validBody;
    private ResponseEntity<String> loginResponse;
    private UsersLoginBody invalidBody;
    @Given("I have invalid credentials to log in")
    public void iHaveInvalidCredentialsToLogIn() {
        invalidBody = new UsersLoginBody("mark@bbcbank.nl", "invalidPassword");
    }

    @When("I call endpoint to log into the system with invalid credentials")
    public void iCallEndpointToLogIntoTheSystemWithInvalidCredentials() throws JsonProcessingException {
        loginResponse = callPostHttpHeadersWithoutJwt(invalidBody);
    }

    @Then("I receive http code {int} not authorized to log in")
    public void iReceiveHttpCodeNotAuthorizedToLogIn(int status) {
        Assertions.assertEquals(status, loginResponse.getStatusCodeValue());
    }




    @Given("I have valid credentials to log in")
    public void iHaveValidCredentialsToLogIn() {
        validBody = new UsersLoginBody("mark@bbcbank.nl", "MarkTest");
    }

    @When("I call endpoint to log into the system")
    public void iCallEndpointToLogIntoTheSystem() throws JsonProcessingException {
        loginResponse = callPostHttpHeadersWithoutJwt(validBody);
    }

    @Then("I receive http code {int} for loggin into the system")
    public void iReceiveHttpCodeForLogginIntoTheSystem(int status) {
        Assertions.assertEquals(status, loginResponse.getStatusCodeValue());
    }

    @And("I receive valid jwt token")
    public void iReceiveValidJwtToken() throws JSONException {
        JSONObject obj = new JSONObject(loginResponse.getBody());
        Assertions.assertTrue(obj.getString("token").startsWith("ey"));
    }
}
