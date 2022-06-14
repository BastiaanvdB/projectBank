package io.swagger;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "io.swagger.steps",
        plugin = "pretty",
        publish = true)
public class CucumberIT {
}
