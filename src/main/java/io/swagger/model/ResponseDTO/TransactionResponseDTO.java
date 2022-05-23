package io.swagger.model.ResponseDTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * TransactionResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class TransactionResponseDTO   {
  @JsonProperty("ibanFrom")
  private String ibanFrom = null;

  @JsonProperty("ibanTo")
  private String ibanTo = null;

  @JsonProperty("issuedBy")
  private Integer issuedBy = null;

  @JsonProperty("amount")
  private BigDecimal amount = null;

  @JsonProperty("iat")
  private Integer iat = null;

  public TransactionResponseDTO ibanFrom(String ibanFrom) {
    this.ibanFrom = ibanFrom;
    return this;
  }

  /**
   * Get ibanFrom
   * @return ibanFrom
   **/
  @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
      @NotNull

  @Size(min=18,max=18)   public String getIbanFrom() {
    return ibanFrom;
  }

  public void setIbanFrom(String ibanFrom) {
    this.ibanFrom = ibanFrom;
  }

  public TransactionResponseDTO ibanTo(String ibanTo) {
    this.ibanTo = ibanTo;
    return this;
  }

  /**
   * Get ibanTo
   * @return ibanTo
   **/
  @Schema(example = "NLxxINHO0xxxxxxxxx", required = true, description = "")
      @NotNull

  @Size(min=18,max=18)   public String getIbanTo() {
    return ibanTo;
  }

  public void setIbanTo(String ibanTo) {
    this.ibanTo = ibanTo;
  }

  public TransactionResponseDTO issuedBy(Integer issuedBy) {
    this.issuedBy = issuedBy;
    return this;
  }

  /**
   * Get issuedBy
   * @return issuedBy
   **/
  @Schema(example = "1", required = true, description = "")
      @NotNull

    public Integer getIssuedBy() {
    return issuedBy;
  }

  public void setIssuedBy(Integer issuedBy) {
    this.issuedBy = issuedBy;
  }

  public TransactionResponseDTO amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Get amount
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

  public TransactionResponseDTO iat(Integer iat) {
    this.iat = iat;
    return this;
  }

  /**
   * Get iat
   * @return iat
   **/
  @Schema(example = "1650466380", required = true, description = "")
      @NotNull

    public Integer getIat() {
    return iat;
  }

  public void setIat(Integer iat) {
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
    TransactionResponseDTO transactionResponseDTO = (TransactionResponseDTO) o;
    return Objects.equals(this.ibanFrom, transactionResponseDTO.ibanFrom) &&
        Objects.equals(this.ibanTo, transactionResponseDTO.ibanTo) &&
        Objects.equals(this.issuedBy, transactionResponseDTO.issuedBy) &&
        Objects.equals(this.amount, transactionResponseDTO.amount) &&
        Objects.equals(this.iat, transactionResponseDTO.iat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ibanFrom, ibanTo, issuedBy, amount, iat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TransactionResponseDTO {\n");

    sb.append("    ibanFrom: ").append(toIndentedString(ibanFrom)).append("\n");
    sb.append("    ibanTo: ").append(toIndentedString(ibanTo)).append("\n");
    sb.append("    issuedBy: ").append(toIndentedString(issuedBy)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
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
