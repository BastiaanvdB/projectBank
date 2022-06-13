package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * PinAuthenticateResponseDTO
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PinAuthenticateResponseDTO {

    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("pincode")
    private String pincode = null;

    @JsonProperty("isValid")
    private Boolean isValid = null;
}
