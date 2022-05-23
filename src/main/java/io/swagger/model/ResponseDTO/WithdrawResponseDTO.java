package io.swagger.model.ResponseDTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * WithdrawResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class WithdrawResponseDTO   {
  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("location")
  private String location = null;

  @JsonProperty("iat")
  private BigDecimal iat = null;

  public WithdrawResponseDTO iban(String iban) {
    this.iban = iban;
    return this;
  }

  /**
   * Get iban
   * @return iban
   **/
  @Schema(example = "NLxxABNAxxxxxxxxxx", required = true, description = "")
      @NotNull

  @Size(min=18,max=18)   public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public WithdrawResponseDTO amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
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

  public WithdrawResponseDTO location(String location) {
    this.location = location;
    return this;
  }

  /**
   * Get location
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

  public WithdrawResponseDTO iat(BigDecimal iat) {
    this.iat = iat;
    return this;
  }

  /**
   * Get iat
   * @return iat
   **/
  @Schema(example = "1650466380", required = true, description = "")
      @NotNull

    @Valid
    public BigDecimal getIat() {
    return iat;
  }

  public void setIat(BigDecimal iat) {
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
    WithdrawResponseDTO withdrawResponseDTO = (WithdrawResponseDTO) o;
    return Objects.equals(this.iban, withdrawResponseDTO.iban) &&
        Objects.equals(this.amount, withdrawResponseDTO.amount) &&
        Objects.equals(this.location, withdrawResponseDTO.location) &&
        Objects.equals(this.iat, withdrawResponseDTO.iat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, amount, location, iat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WithdrawResponseDTO {\n");

    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
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
