package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enumeration.Role;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
    private String address;
    private String city;
    private String postalCode;
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    private String phone;
    private BigDecimal transactionLimit;
    private BigDecimal dayLimit;
    private Boolean activated;
    private String password;

    public User() {

    }

    public User(Integer id, String firstname, String lastname, String address, String city, String postalCode, String email, List<Role> roles, String phone, BigDecimal transactionLimit, BigDecimal dayLimit, Boolean activated, String password) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.email = email;
        this.roles = roles;
        this.phone = phone;
        this.transactionLimit = transactionLimit;
        this.dayLimit = dayLimit;
        this.activated = activated;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getTransactionLimit() {
        return transactionLimit;
    }

    public void setTransactionLimit(BigDecimal transactionLimit) {
        this.transactionLimit = transactionLimit;
    }

    public BigDecimal getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(BigDecimal dayLimit) {
        this.dayLimit = dayLimit;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
