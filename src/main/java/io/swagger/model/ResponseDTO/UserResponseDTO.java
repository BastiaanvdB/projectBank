package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UserResponseDTO
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2022-05-17T11:45:05.257Z[GMT]")


public class UserResponseDTO {
    @JsonProperty("id")
    private Integer id = null;

    @JsonProperty("firstname")
    private String firstname = null;

    @JsonProperty("lastname")
    private String lastname = null;

    @JsonProperty("address")
    private String address = null;

    @JsonProperty("city")
    private String city = null;

    @JsonProperty("postalCode")
    private String postalCode = null;

    @JsonProperty("email")
    private String email = null;

    @JsonProperty("role")
    private List<Integer> roles = new ArrayList<>();

    @JsonProperty("phone")
    private String phone = null;

    @JsonProperty("transaction_Limit")
    private BigDecimal transactionLimit = null;

    @JsonProperty("day_limit")
    private BigDecimal dayLimit = null;

    @JsonProperty("activated")
    private Boolean activated = null;

    public UserResponseDTO id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @Schema(example = "1", description = "")

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserResponseDTO firstname(String firstname) {
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

    public UserResponseDTO lastname(String lastname) {
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

    public UserResponseDTO address(String address) {
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

    public UserResponseDTO city(String city) {
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

    public UserResponseDTO postalCode(String postalCode) {
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

    public UserResponseDTO email(String email) {
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


    public List<Integer> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        for (Role role : roles) {
            this.roles.add(role.ordinal());
        }
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    /**
     * Get role
     *
     * @return role
     **/


    public UserResponseDTO phone(String phone) {
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

    public UserResponseDTO transactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
        return this;
    }

    /**
     * Get transactionLimit
     *
     * @return transactionLimit
     **/
    @Schema(example = "2000", required = true, description = "")
    @NotNull

    @Valid
    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public UserResponseDTO dayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
        return this;
    }

    /**
     * Get dayLimit
     *
     * @return dayLimit
     **/
    @Schema(example = "5000", description = "")

    @Valid
    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public UserResponseDTO activated(Boolean activated) {
        this.activated = activated;
        return this;
    }

    /**
     * Get activated
     *
     * @return activated
     **/
    @Schema(example = "true", required = true, description = "")
    @NotNull

    public Boolean isActivated() {
        return activated;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserResponseDTO userResponseDTO = (UserResponseDTO) o;
        return Objects.equals(this.id, userResponseDTO.id) &&
                Objects.equals(this.firstname, userResponseDTO.firstname) &&
                Objects.equals(this.lastname, userResponseDTO.lastname) &&
                Objects.equals(this.address, userResponseDTO.address) &&
                Objects.equals(this.city, userResponseDTO.city) &&
                Objects.equals(this.postalCode, userResponseDTO.postalCode) &&
                Objects.equals(this.email, userResponseDTO.email) &&
                Objects.equals(this.roles, userResponseDTO.roles) &&
                Objects.equals(this.phone, userResponseDTO.phone) &&
                Objects.equals(this.transactionLimit, userResponseDTO.transactionLimit) &&
                Objects.equals(this.dayLimit, userResponseDTO.dayLimit) &&
                Objects.equals(this.activated, userResponseDTO.activated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstname, lastname, address, city, postalCode, email, roles, phone, transactionLimit, dayLimit, activated);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserResponseDTO {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    firstname: ").append(toIndentedString(firstname)).append("\n");
        sb.append("    lastname: ").append(toIndentedString(lastname)).append("\n");
        sb.append("    address: ").append(toIndentedString(address)).append("\n");
        sb.append("    city: ").append(toIndentedString(city)).append("\n");
        sb.append("    postalCode: ").append(toIndentedString(postalCode)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    role: ").append(toIndentedString(roles)).append("\n");
        sb.append("    phone: ").append(toIndentedString(phone)).append("\n");
        sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
        sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
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
