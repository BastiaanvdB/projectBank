package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * PinAuthenticateDTO
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PinAuthenticateDTO {

    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("pincode")
    private String pincode = null;

}
