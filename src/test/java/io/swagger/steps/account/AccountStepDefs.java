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

public class AccountStepDefs extends BaseStepDefinitions {

    private LoginDTO loginDTO;
    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private ResponseEntity<String> response;

    private String getAccountsToken;
    private String invalidJwtToken;
    private ResponseEntity<String> getAccountsResponse;
    private ResponseEntity<String> invalidAccountResponseForbidden;
    private String userId;

    private String getJwt() throws JSONException, JsonProcessingException {
        loginDTO = new LoginDTO();
        loginDTO.setEmail("employee");
        loginDTO.setPassword("password");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");

        HttpEntity<String> request = new HttpEntity<String>(mapper.writeValueAsString(
                loginDTO),
                httpHeaders);
        response = restTemplate.postForEntity(getBaseUrl() + "/users/login",
                request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("token");
    }

    private ResponseEntity<String> callGetHttpHeaders(String token, String url) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);

        return new TestRestTemplate().exchange(
                getBaseUrl() + url, HttpMethod.GET, new HttpEntity<Object>(httpHeaders),
                String.class);
    }






    @Given("I have valid jwt to get all accounts")
    public void iHaveAValidUserObject() throws JSONException, JsonProcessingException {
        getAccountsToken = getJwt();
        Assertions.assertTrue(getJwt().startsWith("ey"));
    }

    @When("I call endpoint to get all accounts")
    public void iCallEndpointToGetAllUsers() throws JsonProcessingException {
        getAccountsResponse = callGetHttpHeaders(getAccountsToken, "/accounts");
    }

    @Then("I receive a status of success of {int}")
    public void iReceiveAStatusOfSuccessOf(int status) {
        Assertions.assertEquals(status, getAccountsResponse.getStatusCodeValue());
    }

    @And("I get a list of accounts back")
    public void iGetAListOfAccountsBack() throws JSONException {
        JSONObject jsonObject = new JSONObject(getAccountsResponse.getBody());
        String accountEntityList = jsonObject.getString("accountEntityList");
        Assertions.assertTrue(accountEntityList.contains("iban"));
    }
}
