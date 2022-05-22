package io.swagger.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enumeration.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

}
