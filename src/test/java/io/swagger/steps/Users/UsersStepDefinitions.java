package io.swagger.steps.Users;

import io.swagger.steps.BaseStepDefinitions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersStepDefinitions extends BaseStepDefinitions {


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




}
