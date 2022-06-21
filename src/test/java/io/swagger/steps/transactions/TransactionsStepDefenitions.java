package io.swagger.steps.transactions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TransactionsStepDefenitions extends BaseStepDefinitions {

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

    private ResponseEntity<String> response;



    // Sen 1

    @Given("I have valid jwt to create new transaction")
    public void iHaveAValidUser() {
        Assertions.assertTrue(validTokenUser.startsWith("ey"));
    }

    @When("I call endpoint with post request to create new transaction")
    public void callEndpointWithPostRequestToCreateNewTransactionWithValidJwt() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0000000002");
        body.put("ibanTo", "NL01INHO0000000003");
        body.put("pin", "1234");
        body.put("amount", "10.50");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }

    @And("I receive newly created transaction")
    public void receiveCreatedTransaction() throws JSONException {
        JSONObject obj = new JSONObject(response.getBody());
        Assertions.assertNotNull(obj.getString("iat"));
    }



    // Sen 2
    @Given("I have valid jwt for user with a account")
    public void iHaveValidJwtForUserWithAAccount() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I call endpoint with get request to get all transactions from current user")
    public void iCallEndpointWithGetRequestToGetAllTransactionsFromCurrentUser() {
        response = callGetHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002/transactions");
    }

    @Then("I receive http code {int} ok")
    public void iReceiveHttpCodeOk(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }

    @And("I receive list with transactions")
    public void iReceiveListWithTransactions() throws JSONException {
        JSONArray obj = new JSONArray(response.getBody());
        String first = obj.getString(0);
        Assertions.assertTrue(first.contains("iat"));
    }



    // 3
    @Given("I have valid jwt for user with a savings account")
    public void iHaveValidJwtForUserWithASavingsAccount() {
        Assertions.assertTrue(validTokenEmployee.startsWith("ey"));
    }

    @When("I do a transfer from my savings account to another savings account that not belongs to me")
    public void iDoATransferFromMySavingsAccountToAnotherSavingsAccountThatNotBelongsToMe() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0999999996");
        body.put("ibanTo", "NL01INHO0999999996");
        body.put("pin", "1255");
        body.put("amount", "10.50");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }

    @Then("I receive http code {int} This account does not belong to U")
    public void iReceiveHttpCodeThisAccountDoesNotBelongToU(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }



    // 4
    @When("I do a transfer from my savings account to another account that not belongs to me")
    public void iDoATransferFromMySavingsAccountToAnotherAccountThatNotBelongsToMe() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0999999996");
        body.put("ibanTo", "NL01INHO0000000003");
        body.put("pin", "1255");
        body.put("amount", "10.50");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }



    // 5
    @When("I call this endpoint with post request to deposit money on my account")
    public void iCallThisEndpointWithPostRequestToDepositMoneyOnMyAccount() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("amount", "10.50");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002/deposit", String.valueOf(body));
    }

    @And("I receive the deposit transaction")
    public void iReceiveTheDepositTransaction() throws JSONException {
        JSONObject obj = new JSONObject(response.getBody());
        Assertions.assertNotNull(obj.getString("iat"));
    }



    // 6
    @When("I call this endpoint with post request to withdraw money from my account")
    public void iCallThisEndpointWithPostRequestToWithdrawMoneyFromMyAccount() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("amount", "10.50");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002/withdraw", String.valueOf(body));
    }

    @And("I receive the withdraw transaction")
    public void iReceiveTheWithdrawTransaction() throws JSONException {
        JSONObject obj = new JSONObject(response.getBody());
        Assertions.assertNotNull(obj.getString("iat"));
    }



    // 7
    @When("I do a transaction that exceeds the absolute limit of the account")
    public void iDoATransactionThatExceedsTheAbsoluteLimitOfTheAccount() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0999999998");
        body.put("ibanTo", "NL01INHO0999999997");
        body.put("pin", "1255");
        body.put("amount", "201");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }

    @Then("I receive http code {int} Not enough money on this account")
    public void iReceiveHttpCodeNotEnoughMoneyOnThisAccount(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }



    // 8
    @When("I do a transaction that exceeds the day limit of the user")
    public void iDoATransactionThatExceedsTheDayLimitOfTheUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0000000002");
        body.put("ibanTo", "NL01INHO0000000003");
        body.put("pin", "1234");
        body.put("amount", "1001");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }

    @Then("I receive http code {int} With this transaction day limit will be exceeded")
    public void iReceiveHttpCodeWithThisTransactionDayLimitWillBeExceeded(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }



    // 9
    @When("I do a transaction that exceeds the transaction limit of the user")
    public void iDoATransactionThatExceedsTheTransactionLimitOfTheUser() throws JSONException {
        JSONObject body = new JSONObject();
        body.put("ibanFrom", "NL01INHO0000000002");
        body.put("ibanTo", "NL01INHO0000000003");
        body.put("pin", "1234");
        body.put("amount", "1501");
        response = callPostHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/transactions", String.valueOf(body));
    }

    @Then("I receive http code {int} This transaction exceeds the transaction limit")
    public void iReceiveHttpCodeThisTransactionExceedsTheTransactionLimit(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }



    // 10
    @When("I call endpoint to get specific transactions with parameters in url")
    public void iCallEndpointToGetSpecificTransactionsWithParametersInUrl() {
        response = callGetHttpHeaders(validTokenUser, "/Groep1BankApi/bank/1.0.0/accounts/NL01INHO0000000002/transactions?balance operator=>=&Balance=1&offset=0&limit=10&start_date=2022-06-07&end_date=2022-07-17");
    }

    @Then("I will receive http code {int} created")
    public void iWillReceiveHttpCodeCreated(int status) {
        Assertions.assertEquals(status, response.getStatusCodeValue());
    }
}
