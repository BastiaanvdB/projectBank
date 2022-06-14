package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * UserRoleDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class UserRoleDTO {
    @JsonProperty("roles")
    @NotNull(message = "Please enter a role")
    @NotEmpty(message = "Please enter a role")
    private List<Integer> roles = new ArrayList<>();


    public UserRoleDTO roles(List<Integer> roles) {
        this.roles = roles;
        return this;
    }

    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Integer> roles) {
        this.roles = roles;
    }
}
