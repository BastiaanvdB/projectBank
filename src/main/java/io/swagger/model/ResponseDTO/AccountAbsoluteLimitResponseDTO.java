package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

/**
 * AccountAbsoluteLimitResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-22T20:41:29.160Z[GMT]")


public class AccountAbsoluteLimitResponseDTO   {
    @JsonProperty("absolute_Limit")
    private BigDecimal absoluteLimit = null;

    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }
}
