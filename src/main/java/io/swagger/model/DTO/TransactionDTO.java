package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.exception.SameAccountException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * TransactionDTO
 */
@Validated
@Getter
@Setter
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class TransactionDTO {
    @JsonProperty("ibanFrom")
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @Size(min = 18, max = 18, message = "Please enter a valid 18 character iban!")
    @NotBlank(message = "Please enter iban from!")
    @NotEmpty(message = "Please enter iban from!")
    @NotNull(message = "Please enter iban from!")
    private String ibanFrom = null;

    @JsonProperty("ibanTo")
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @Size(min = 18, max = 18, message = "Please enter a valid 18 character iban!")
    @NotBlank(message = "Please enter iban to!")
    @NotEmpty(message = "Please enter iban to!")
    @NotNull(message = "Please enter iban to!")
    private String ibanTo = null;

    @JsonProperty("pin")
    @NotBlank(message = "Please enter pin!")
    @NotEmpty(message = "Please enter pin!")
    @NotNull(message = "Please enter pin!")
    private String pin = null;

    @JsonProperty("amount")
    @DecimalMin(value = "0.01", message = "Please enter a valid transaction amount!")
    private BigDecimal amount = null;

    public void setIbanFrom(String ibanFrom) throws SameAccountException {
        if (this.ibanTo == ibanFrom) {
            throw new SameAccountException("Sorry the account is the same, we can't perform this action!");
        } else {
            this.ibanFrom = ibanFrom;
        }
    }

    public void setIbanTo(String ibanTo) throws SameAccountException {
        if (this.ibanFrom == ibanTo) {
            throw new SameAccountException("Sorry the account is the same, we can't perform this action!");
        } else {
            this.ibanTo = ibanTo;
        }
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransactionDTO transactionDTO = (TransactionDTO) o;
        return Objects.equals(this.ibanFrom, transactionDTO.ibanFrom) &&
                Objects.equals(this.ibanTo, transactionDTO.ibanTo) &&
                Objects.equals(this.amount, transactionDTO.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ibanFrom, ibanTo, amount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class TransactionDTO {\n");

        sb.append("    ibanFrom: ").append(toIndentedString(ibanFrom)).append("\n");
        sb.append("    ibanTo: ").append(toIndentedString(ibanTo)).append("\n");
        sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
