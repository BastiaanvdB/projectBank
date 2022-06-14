package io.swagger.steps;

import io.swagger.CucumberContextConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(classes = CucumberContextConfig.class)
public class BaseStepDefinitions {

    @LocalServerPort
    private int port;

    private String base = "http://localhost:";

    public String getBaseUrl() {
        return base + port;
    }
}
