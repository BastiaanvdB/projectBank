package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enumeration.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * AccountDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class AccountDTO {

    @JsonProperty("type")
    private AccountType type = null;

    @JsonProperty("user_Id")
    private Integer userId = null;

    public AccountDTO type(AccountType type) {
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

    public AccountDTO userId(Integer userId) {
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

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccountDTO accountDTO = (AccountDTO) o;
        return Objects.equals(this.type, accountDTO.type) &&
                Objects.equals(this.userId, accountDTO.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, userId);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccountDTO {\n");

        sb.append("    type: ").append(toIndentedString(type)).append("\n");
        sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
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
