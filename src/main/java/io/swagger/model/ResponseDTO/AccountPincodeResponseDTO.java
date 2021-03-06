package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

/**
 * AccountPincodeResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-22T20:41:29.160Z[GMT]")


public class AccountPincodeResponseDTO {
    @JsonProperty("newPincode")
    private String newPincode = null;

    public String getNewPincode() {
        return newPincode;
    }

    public void setNewPincode(String newPincode) {
        this.newPincode = newPincode;
    }
}
