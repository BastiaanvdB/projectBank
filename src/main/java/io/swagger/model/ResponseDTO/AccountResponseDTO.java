package io.swagger.model.ResponseDTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AccountResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class AccountResponseDTO   {
  @JsonProperty("iban")
  private String iban = null;

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    CURRENT("Current"),

    SAVINGS("Savings");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("pin")
  private Integer pin = null;

  @JsonProperty("user_Id")
  private Integer userId = null;

  @JsonProperty("employee_Id")
  private Integer employeeId = null;

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
   * @return iban
   **/
  @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
      @NotNull

  @Size(min=18,max=18)   public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public AccountResponseDTO type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   **/
  @Schema(example = "Current", required = true, description = "")
      @NotNull

    public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public AccountResponseDTO pin(Integer pin) {
    this.pin = pin;
    return this;
  }

  /**
   * Get pin
   * @return pin
   **/
  @Schema(example = "1234", description = "")

    public Integer getPin() {
    return pin;
  }

  public void setPin(Integer pin) {
    this.pin = pin;
  }

  public AccountResponseDTO userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
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

  public AccountResponseDTO employeeId(Integer employeeId) {
    this.employeeId = employeeId;
    return this;
  }

  /**
   * Get employeeId
   * @return employeeId
   **/
  @Schema(example = "2", required = true, description = "")
      @NotNull

    public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  public AccountResponseDTO balance(BigDecimal balance) {
    this.balance = balance;
    return this;
  }

  /**
   * Get balance
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
        Objects.equals(this.employeeId, accountResponseDTO.employeeId) &&
        Objects.equals(this.balance, accountResponseDTO.balance) &&
        Objects.equals(this.absoluteLimit, accountResponseDTO.absoluteLimit) &&
        Objects.equals(this.activated, accountResponseDTO.activated);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, type, pin, userId, employeeId, balance, absoluteLimit, activated);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountResponseDTO {\n");

    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    employeeId: ").append(toIndentedString(employeeId)).append("\n");
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
