package io.swagger.steps.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.steps.BaseStepDefinitions;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccountStepDefinitions extends BaseStepDefinitions {

    private final String validTokenEmployee = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYXJrQGJiY2JhbmsubmwiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX0VNUExPWUVFIn0seyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTMwNzA1NywiZXhwIjoxNjU2MTcxMDU3fQ.LsJXusVw8ic2nrn_My8pQ8X-iTgEF_pLu2yawquDMxU";
    private final String validTokenUser = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicmFtQGxpdmUubmwiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTMwNzA5MCwiZXhwIjoxNjU2MTcxMDkwfQ.RLQ19uw6HYVY0JE99V_jDkLVtwsFnZw7DR54EMgqfBI";

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





    private ResponseEntity<String> accounts;
    @Given("I have valid jwt to get all accounts")
    public void iHaveAValidUserObject() throws JSONException, JsonProcessingException {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint to get all accounts")
    public void iCallEndpointToGetAllAccounts() {
        accounts = callGetHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/accounts");
    }

    @Then("I receive http code {int} ok for all accounts")
    public void iReceiveAStatusOfSuccessOf(int status) {
        Assertions.assertEquals(status, accounts.getStatusCodeValue());
    }

    @And("I get list with accounts")
    public void iGetAListOfAccountsBack() throws JSONException {
        JSONArray obj = new JSONArray(accounts.getBody());
        String firstAccount = obj.getString(0);
        Assertions.assertTrue(firstAccount.contains("iban"));
    }




    private ResponseEntity<String> createResponse;
    @Given("I have valid jwt to create new account")
    public void iHaveValidJwtToCreateNewAccount() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint with post request to create new account")
    public void iCallEndpointWithPostRequestToCreateNewAccount() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("type", "Current");
        body.put("user_Id", 1);
        createResponse = callPostHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/accounts", String.valueOf(body));
    }

    @Then("I receive http code {int} created")
    public void iReceiveHttpCodeCreated(int status) {
        Assertions.assertEquals(status, createResponse.getStatusCodeValue());
    }

    @And("I receive newly created account")
    public void iReceiveNewlyCreatedAccount() throws JSONException {
        JSONObject obj = new JSONObject(createResponse.getBody());
        Assertions.assertTrue(obj.getString("iban").startsWith("NL"));
    }



    private ResponseEntity<String> accountsForUser;
    @Given("I have valid jwt to get all accounts for specific user")
    public void iHaveValidJwtToGetAllAccountsForSpecificUser() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint with get request to get all accounts for user")
    public void iCallEndpointWithGetRequestToGetAllAccountsForUser() {
        accountsForUser = callGetHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/users/2/accounts");
    }

    @Then("I receive http code {int} ok for all accounts user")
    public void iReceiveHttpCodeOkForAllAccountsUser(int status) {
        Assertions.assertEquals(status, accountsForUser.getStatusCodeValue());
    }

    @And("I receive list with accounts of user")
    public void iReceiveListWithAccountsOfUser() throws JSONException {
        JSONArray obj = new JSONArray(accountsForUser.getBody());
        String firstAccount = obj.getString(0);
        Assertions.assertTrue(firstAccount.contains("iban"));
    }



    private ResponseEntity<String> accountByIban;
    @Given("I have valid jwt to get one account with specific iban")
    public void iHaveValidJwtToGetOneAccountWithSpecificIban() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint with get request to get one account")
    public void iCallEndpointWithGetRequestToGetOneAccount() {
        accountByIban = callGetHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002");
    }

    @Then("I receive http code {int} ok for one account")
    public void iReceiveHttpCodeOkForOneAccount(int status) {
        Assertions.assertEquals(status, accountByIban.getStatusCodeValue());
    }

    @And("I receive one account with the specified iban")
    public void iReceiveOneAccountWithTheSpecifiedIban() throws JSONException {
        JSONObject obj = new JSONObject(accountByIban.getBody());
        Assertions.assertTrue(obj.getString("iban").startsWith("NL"));
    }



    private ResponseEntity<String> updatedAccount;
    @Given("I have valid jwt to update status")
    public void iHaveValidJwtToUpdateStatus() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint with put request to update status")
    public void iCallEndpointWithPutRequestToUpdateStatus() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("activated", false);
        updatedAccount = callPutHttpHeaders(validTokenEmployee, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002/activation", String.valueOf(body));
    }

    @Then("I receive http code {int} ok for updated account")
    public void iReceiveHttpCodeOkForUpdatedAccount(int status) {
        Assertions.assertEquals(status, updatedAccount.getStatusCodeValue());
    }

    @Then("I receive the updated account")
    public void iReceiveTheUpdatedAccount() throws JSONException {
        JSONObject obj = new JSONObject(updatedAccount.getBody());
        Assertions.assertEquals("false", obj.getString("activated"));
    }




    @Given("I have valid jwt for user")
    public void iDontHaveValidJwt() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint to get all accounts without employee jwt")
    public void iCallEndpointToGetAllAccountsWithoutValidJwt() {
        accounts = callGetHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/accounts");
    }

    @Then("I receive http code {int} bad request")
    public void iReceiveHttpCodUnauthorized(int status) {
        Assertions.assertEquals(status, accounts.getStatusCodeValue());
    }
}
