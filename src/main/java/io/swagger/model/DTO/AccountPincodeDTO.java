package io.swagger.model.DTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * AccountPincodeDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class AccountPincodeDTO   {
  @JsonProperty("oldPincode")
  private Integer oldPincode = null;

  @JsonProperty("newPincode")
  private Integer newPincode = null;

  public AccountPincodeDTO oldPincode(Integer oldPincode) {
    this.oldPincode = oldPincode;
    return this;
  }

  /**
   * Get oldPincode
   * @return oldPincode
   **/
  @Schema(example = "1234", required = true, description = "")
      @NotNull

    public Integer getOldPincode() {
    return oldPincode;
  }

  public void setOldPincode(Integer oldPincode) {
    this.oldPincode = oldPincode;
  }

  public AccountPincodeDTO newPincode(Integer newPincode) {
    this.newPincode = newPincode;
    return this;
  }

  /**
   * Get newPincode
   * @return newPincode
   **/
  @Schema(example = "4321", required = true, description = "")
      @NotNull

    public Integer getNewPincode() {
    return newPincode;
  }

  public void setNewPincode(Integer newPincode) {
    this.newPincode = newPincode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountPincodeDTO accountPincodeDTO = (AccountPincodeDTO) o;
    return Objects.equals(this.oldPincode, accountPincodeDTO.oldPincode) &&
        Objects.equals(this.newPincode, accountPincodeDTO.newPincode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(oldPincode, newPincode);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AccountPincodeDTO {\n");
    
    sb.append("    oldPincode: ").append(toIndentedString(oldPincode)).append("\n");
    sb.append("    newPincode: ").append(toIndentedString(newPincode)).append("\n");
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
