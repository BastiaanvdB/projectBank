package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * DepositResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class DepositResponseDTO {
    @JsonProperty("iban")
    private String ibanTo = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    @JsonProperty("location")
    private String location = null;

    @JsonProperty("iat")
    private Timestamp iat = null;

    public DepositResponseDTO iban(String iban) {
        this.ibanTo = iban;
        return this;
    }

    /**
     * Get iban
     *
     * @return iban
     **/
    @Schema(example = "NLxxABNAxxxxxxxxxx", required = true, description = "")
    @NotNull

    @Size(min = 18, max = 18)
    public String getIbanTo() {
        return ibanTo;
    }

    public void setIbanTo(String ibanTo) {
        this.ibanTo = ibanTo;
    }

    public DepositResponseDTO amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Get amount
     *
     * @return amount
     **/
    @Schema(example = "150", required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DepositResponseDTO location(String location) {
        this.location = location;
        return this;
    }

    /**
     * Get location
     *
     * @return location
     **/
    @Schema(example = "ATM Haarlem", required = true, description = "")
    @NotNull

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public DepositResponseDTO iat(Timestamp iat) {
        this.iat = iat;
        return this;
    }

    /**
     * Get iat
     *
     * @return iat
     **/
    @Schema(example = "1650466380", required = true, description = "")
    @NotNull

    @Valid
    public Timestamp getIat() {
        return iat;
    }

    public void setIat(Timestamp iat) {
        this.iat = iat;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DepositResponseDTO depositResponseDTO = (DepositResponseDTO) o;
        return Objects.equals(this.ibanTo, depositResponseDTO.ibanTo) &&
                Objects.equals(this.amount, depositResponseDTO.amount) &&
                Objects.equals(this.location, depositResponseDTO.location) &&
                Objects.equals(this.iat, depositResponseDTO.iat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ibanTo, amount, location, iat);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class DepositResponseDTO {\n");

        sb.append("    iban: ").append(toIndentedString(ibanTo)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    iat: ").append(toIndentedString(iat)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
