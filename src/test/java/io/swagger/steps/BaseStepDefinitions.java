package io.swagger.steps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = CucumberContextConfig.class)
public class BaseStepDefinitions {

    @LocalServerPort
    private int port;

    @Value("${io.swagger.api.baseurl}")
    private String base;

    public String getBaseUrl() {
        return base + port;
    }
}
