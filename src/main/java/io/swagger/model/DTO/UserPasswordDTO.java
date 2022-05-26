package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * UserPasswordDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class UserPasswordDTO {
    @JsonProperty("oldPassword")
    private String oldPassword = null;

    @JsonProperty("newPassword")
    private String newPassword = null;

    public UserPasswordDTO oldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    /**
     * Get oldPassword
     *
     * @return oldPassword
     **/
    @Schema(example = "1234", required = true, description = "")
    @NotNull

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public UserPasswordDTO newPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    /**
     * Get newPassword
     *
     * @return newPassword
     **/
    @Schema(example = "4321", required = true, description = "")
    @NotNull

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPasswordDTO userPasswordDTO = (UserPasswordDTO) o;
        return Objects.equals(this.oldPassword, userPasswordDTO.oldPassword) &&
                Objects.equals(this.newPassword, userPasswordDTO.newPassword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(oldPassword, newPassword);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserPasswordDTO {\n");

        sb.append("    oldPassword: ").append(toIndentedString(oldPassword)).append("\n");
        sb.append("    newPassword: ").append(toIndentedString(newPassword)).append("\n");
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
