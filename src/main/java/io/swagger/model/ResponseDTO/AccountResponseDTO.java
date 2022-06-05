package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enumeration.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * AccountResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class AccountResponseDTO {
    @JsonProperty("iban")
    private String iban = null;

    @JsonProperty("type")
    private AccountType type = null;

    @JsonProperty("pin")
    private String pin = null;

    @JsonProperty("user_Id")
    private Integer userId = null;

    @JsonProperty("balance")
    private BigDecimal balance = null;

    @JsonProperty("absolute_Limit")
    private BigDecimal absoluteLimit = null;

    @JsonProperty("activated")
    private Boolean activated = null;

    public AccountResponseDTO iban(String iban) {
        this.iban = iban;
        return this;
    }

    /**
     * Get iban
     *
     * @return iban
     **/
    @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
    @NotNull

    @Size(min = 18, max = 18)
    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public AccountResponseDTO type(AccountType type) {
        this.type = type;
        return this;
    }

    /**
     * Get type
     *
     * @return type
     **/
    @Schema(example = "Current", required = true, description = "")
    @NotNull

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public AccountResponseDTO pin(String pin) {
        this.pin = pin;
        return this;
    }

    /**
     * Get pin
     *
     * @return pin
     **/
    @Schema(example = "1234", description = "")

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public AccountResponseDTO userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Get userId
     *
     * @return userId
     **/
    @Schema(example = "1", required = true, description = "")
    @NotNull

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Get balance
     *
     * @return balance
     **/
    @Schema(required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountResponseDTO absoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
        return this;
    }

    /**
     * Get absoluteLimit
     *
     * @return absoluteLimit
     **/
    @Schema(example = "10", required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public AccountResponseDTO activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    /**
     * Get activated
     *
     * @return activated
     **/
    @Schema(example = "true", required = true, description = "")
    @NotNull

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountResponseDTO accountResponseDTO = (AccountResponseDTO) o;
        return Objects.equals(this.iban, accountResponseDTO.iban) &&
                Objects.equals(this.type, accountResponseDTO.type) &&
                Objects.equals(this.pin, accountResponseDTO.pin) &&
                Objects.equals(this.userId, accountResponseDTO.userId) &&
                //Objects.equals(this.employeeId, accountResponseDTO.employeeId) &&
                Objects.equals(this.balance, accountResponseDTO.balance) &&
                Objects.equals(this.absoluteLimit, accountResponseDTO.absoluteLimit) &&
                Objects.equals(this.activated, accountResponseDTO.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban, type, pin, userId, balance, absoluteLimit, activated);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountResponseDTO {\n");

        sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
        sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
        sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
        sb.append("    activated: ").append(toIndentedString(activated)).append("\n");
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
