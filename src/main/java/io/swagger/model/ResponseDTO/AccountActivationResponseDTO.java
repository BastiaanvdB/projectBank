package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * AccountActivationResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-22T20:41:29.160Z[GMT]")


public class AccountActivationResponseDTO {
    @JsonProperty("activated")
    private Boolean activated = null;

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }
}
