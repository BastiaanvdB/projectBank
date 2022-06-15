package io.swagger.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * UserDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class UserDTO {
    @JsonProperty("firstname")
    @NotBlank(message = "Please enter a firstname")
    @NotNull(message = "Please enter a firstname")
    @NotEmpty(message = "Please enter a firstname")
    @Size(min = 2, message = "Please enter a valid firstname")
    private String firstname = null;

    @NotBlank(message = "Please enter a lastname")
    @NotNull(message = "Please enter a lastname")
    @NotEmpty(message = "Please enter a lastname")
    @Size(min = 2, message = "Please enter a valid lastname")
    @JsonProperty("lastname")
    private String lastname = null;

    @NotBlank(message = "Please enter a address")
    @NotNull(message = "Please enter a address")
    @NotEmpty(message = "Please enter a address")
    @Size(min = 2, message = "Please enter a valid address")
    @JsonProperty("address")
    private String address = null;

    @NotBlank(message = "Please enter a city")
    @NotNull(message = "Please enter a city")
    @NotEmpty(message = "Please enter a city")
    @Size(min = 2, message = "Please enter a valid city")
    @JsonProperty("city")
    private String city = null;

    @NotBlank(message = "Please enter a postalcode")
    @NotNull(message = "Please enter a postalcode")
    @NotEmpty(message = "Please enter a postalcode")
    @Size(min = 6, message = "Please enter a valid postalcode")
    @JsonProperty("postalCode")
    private String postalCode = null;


    @NotNull(message = "Please enter a email")
    @NotEmpty(message = "Please enter a email")
    @Email(message = "Please enter a email")
    @JsonProperty("email")
    private String email = null;

    @NotBlank(message = "Please enter a phone number")
    @NotNull(message = "Please enter a phone number")
    @NotEmpty(message = "Please enter a phone number")
    @Size(min = 10, message = "Please enter a valid phone number")
    @JsonProperty("phone")
    private String phone = null;

    @NotNull(message = "Please enter a day limit")
    @DecimalMin(value = "1", message = "Please enter a valid day limit")
    @JsonProperty("day_limit")
    private BigDecimal dayLimit = null;

    @NotNull(message = "Please enter a transaction limit")
    @DecimalMin(value = "1", message = "Please enter a valid transaction limit")
    @JsonProperty("transaction_Limit")
    private BigDecimal transactionLimit = null;


    public UserDTO firstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    /**
     * Get firstname
     *
     * @return firstname
     **/
    @Schema(example = "Henk", required = true, description = "")
    @NotNull

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public UserDTO lastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    /**
     * Get lastname
     *
     * @return lastname
     **/
    @Schema(example = "Bakker", required = true, description = "")
    @NotNull

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public UserDTO address(String address) {
        this.address = address;
        return this;
    }

    /**
     * Get address
     *
     * @return address
     **/
    @Schema(example = "Bijdorplaan 15", required = true, description = "")
    @NotNull

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public UserDTO city(String city) {
        this.city = city;
        return this;
    }

    /**
     * Get city
     *
     * @return city
     **/
    @Schema(example = "Haarlem", required = true, description = "")
    @NotNull

    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public UserDTO postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    /**
     * Get postalCode
     *
     * @return postalCode
     **/
    @Schema(example = "2015CE", required = true, description = "")
    @NotNull

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public UserDTO email(String email) {
        this.email = email;
        return this;
    }

    /**
     * Get email
     *
     * @return email
     **/
    @Schema(example = "henkbakker@test.nl", required = true, description = "")
    @NotNull

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    /**
     * Get role
     *
     * @return role
     **/
    @Schema(example = "0", required = true, description = "")
    @NotNull


    public UserDTO phone(String phone) {
        this.phone = phone;
        return this;
    }

    /**
     * Get phone
     *
     * @return phone
     **/
    @Schema(example = "0623445321", required = true, description = "")
    @NotNull

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(this.firstname, userDTO.firstname) &&
                Objects.equals(this.lastname, userDTO.lastname) &&
                Objects.equals(this.address, userDTO.address) &&
                Objects.equals(this.city, userDTO.city) &&
                Objects.equals(this.postalCode, userDTO.postalCode) &&
                Objects.equals(this.email, userDTO.email) &&
                Objects.equals(this.phone, userDTO.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, address, city, postalCode, email, phone);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserDTO {\n");

        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
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
