package io.swagger.model.entity;

import io.swagger.model.enumeration.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Account {

    @Id
    private String iban;
    private AccountType type;
    private String pin;
    private Integer employeeId;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private Boolean activated;

    @ManyToOne
    private User user;

    public Account(String iban, AccountType type, String pin, Integer employeeId, BigDecimal balance, BigDecimal absoluteLimit, Boolean activated) {
        this.iban = iban;
        this.type = type;
        this.pin = pin;
        this.employeeId = employeeId;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.activated = activated;
    }
}
