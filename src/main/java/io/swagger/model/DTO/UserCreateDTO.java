package io.swagger.model.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


/**
 * UserCreateDTO
 */
@Validated
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-22T20:41:29.160Z[GMT]")


public class UserCreateDTO {

    @JsonProperty("firstname")
    @NotNull(message = "Please enter a firstname")
    @NotEmpty(message = "Please enter a firstname")
    @Size(min = 2, message = "Please enter a valid firstname")
    private String firstname = null;

    @JsonProperty("lastname")
    @NotNull(message = "Please enter a lastname")
    @NotEmpty(message = "Please enter a lastname")
    @Size(min = 2, message = "Please enter a valid lastname")
    private String lastname = null;

    @JsonProperty("address")
    @NotNull(message = "Please enter a address")
    @NotEmpty(message = "Please enter a address")
    @Size(min = 2, message = "Please enter a valid address")
    private String address = null;

    @JsonProperty("city")
    @NotNull(message = "Please enter a city")
    @NotEmpty(message = "Please enter a city")
    @Size(min = 2, message = "Please enter a valid city")
    private String city = null;

    @JsonProperty("postalCode")
    @NotNull(message = "Please enter a postalcode")
    @NotEmpty(message = "Please enter a postalcode")
    @Size(min = 6, message = "Please enter a valid postalcode")
    private String postalCode = null;

    @JsonProperty("email")
    @NotNull(message = "Please enter a email")
    @NotEmpty(message = "Please enter a email")
    @Email(message = "Please enter a email")
    private String email = null;

    @JsonProperty("password")
    private String password = null;

    @JsonProperty("phone")
    @NotNull(message = "Please enter a phone number")
    @NotEmpty(message = "Please enter a phone number")
    @Size(min = 10, message = "Please enter a valid phone number")
    private String phone = null;

}
