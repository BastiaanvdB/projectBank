package io.swagger.model.ResponseDTO;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

/**
 * AuthorizationResponse
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class AuthorizationResponse   {
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("token")
  private String token = null;

  public AuthorizationResponse id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   **/
  @Schema(example = "1", required = true, description = "")
      @NotNull

    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public AuthorizationResponse token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   **/
  @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhcHBfaWQiOiIxMjMiLCJ1c2VyX2lkIjoiNTYwMTkzIiwiZXhwaXJ5X3RpbWUiOjE2MjcwMjgzOTV9.NNca6rc4XjV-Q5bVZ8n_OGuNCq5L8PQaLFhv_c0DIkA", required = true, description = "")
      @NotNull

    public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuthorizationResponse authorizationResponse = (AuthorizationResponse) o;
    return Objects.equals(this.id, authorizationResponse.id) &&
        Objects.equals(this.token, authorizationResponse.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuthorizationResponse {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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
