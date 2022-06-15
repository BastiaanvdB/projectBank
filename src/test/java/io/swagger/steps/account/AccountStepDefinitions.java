package io.swagger.steps.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.model.DTO.LoginDTO;
import io.swagger.steps.BaseStepDefinitions;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AccountStepDefinitions extends BaseStepDefinitions {

    private LoginDTO loginDTO;
    private ResponseEntity<String> response;
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    private String validToken;
    private ResponseEntity<String> accounts;

    private String getJwt() throws JSONException, JsonProcessingException {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("mark@bbcbank.nl");
        loginDTO.setPassword("MarkTest");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(
                loginDTO),
                httpHeaders);
        response = restTemplate.postForEntity(getBaseUrl() + "/users/login",
                request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("token");
    }

    private ResponseEntity<String> callGetHttpHeaders(String token, String url) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);

        return new TestRestTemplate().exchange(
                getBaseUrl() + url, HttpMethod.GET, new HttpEntity<>(httpHeaders),
                String.class);
    }






    @Given("I have valid jwt to get all accounts")
    public void iHaveAValidUserObject() throws JSONException, JsonProcessingException {
        validToken = getJwt();
        Assertions.assertTrue(getJwt().startsWith("ey"));
    }

    @When("I call endpoint to get all accounts")
    public void iCallEndpointToGetAllAccounts() {
        accounts = callGetHttpHeaders(validToken, "/accounts");
    }

    @Then("I receive http code {int} ok for all accounts")
    public void iReceiveAStatusOfSuccessOf(int status) {
        Assertions.assertEquals(status, accounts.getStatusCodeValue());
    }

    @And("I get list with accounts")
    public void iGetAListOfAccountsBack() throws JSONException {
        JSONObject obj = new JSONObject(accounts.getBody());
        String accountList = obj.getString("accountList");
        Assertions.assertTrue(accountList.contains("iban"));
    }



    @Given("I have valid jwt to create new account")
    public void iHaveValidJwtToCreateNewAccount() {
    }

    @When("I call endpoint with post request to create new account")
    public void iCallEndpointWithPostRequestToCreateNewAccount() {
    }

    @Then("I receive http code {int} created")
    public void iReceiveHttpCodeCreated(int status) {
    }

    @And("I receive newly created account")
    public void iReceiveNewlyCreatedAccount() {
    }




    @Given("I have valid jwt to get all accounts for specific user")
    public void iHaveValidJwtToGetAllAccountsForSpecificUser() {
    }

    @When("I call endpoint with get request to get all accounts for user")
    public void iCallEndpointWithGetRequestToGetAllAccountsForUser() {
    }

    @Then("I receive http code {int} ok for all accounts user")
    public void iReceiveHttpCodeOkForAllAccountsUser(int status) {
    }

    @And("I receive list with accounts of user")
    public void iReceiveListWithAccountsOfUser() {
    }




    @Given("I have valid jwt to get one account with specific iban")
    public void iHaveValidJwtToGetOneAccountWithSpecificIban() {
    }

    @When("I call endpoint with get request to get one account")
    public void iCallEndpointWithGetRequestToGetOneAccount() {
    }

    @Then("I receive http code {int} ok for one account")
    public void iReceiveHttpCodeOkForOneAccount(int status) {
    }

    @And("I receive one account with the specified iban")
    public void iReceiveOneAccountWithTheSpecifiedIban() {
    }




    @Given("I have valid jwt to update status")
    public void iHaveValidJwtToUpdateStatus() {
    }

    @When("I call endpoint with put request to update status")
    public void iCallEndpointWithPutRequestToUpdateStatus() {
    }

    @Then("I receive http code {int} ok for updated account")
    public void iReceiveHttpCodeOkForUpdatedAccount(int status) {
    }

    @Then("I receive the updated account")
    public void iReceiveTheUpdatedAccount() {
    }




    @Given("I dont have valid jwt")
    public void iDontHaveValidJwt() {
    }

    @When("I call endpoint to get all accounts without valid jwt")
    public void iCallEndpointToGetAllAccountsWithoutValidJwt() {
    }

    @Then("I receive http cod {int} unauthorized")
    public void iReceiveHttpCodUnauthorized(int status) {
    }
}
