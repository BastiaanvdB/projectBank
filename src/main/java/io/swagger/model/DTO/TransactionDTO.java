package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * TransactionDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class TransactionDTO {
    @JsonProperty("ibanFrom")
    private String ibanFrom = null;

    @JsonProperty("ibanTo")
    private String ibanTo = null;

    @JsonProperty("pin")
    private String pin = null;

    @JsonProperty("amount")
    private BigDecimal amount = null;

    public TransactionDTO ibanFrom(String ibanFrom) {
        this.ibanFrom = ibanFrom;
        return this;
    }



    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    /**
     * Get ibanFrom
     *
     * @return ibanFrom
     **/
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @NotNull

    @Size(min = 18, max = 18)
    public String getIbanFrom() {
        return ibanFrom;
    }

    public void setIbanFrom(String ibanFrom) {
        this.ibanFrom = ibanFrom;
    }

    public TransactionDTO ibanTo(String ibanTo) {
        this.ibanTo = ibanTo;
        return this;
    }

    /**
     * Get ibanTo
     *
     * @return ibanTo
     **/
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @NotNull

    @Size(min = 18, max = 18)
    public String getIbanTo() {
        return ibanTo;
    }

    public void setIbanTo(String ibanTo) {
        this.ibanTo = ibanTo;
    }

    public TransactionDTO amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Get amount
     *
     * @return amount
     **/
    @Schema(required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
