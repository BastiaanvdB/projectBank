package io.swagger.model.entity;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.enumeration.AccountType;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Account {

    @Id
    private String iban;
    private AccountType type;
    private Integer pin;
    private Integer employeeId;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private Boolean activated;

    @ManyToOne
    private User user;

    public Account() {
    }

    public Account(String iban, AccountType type, Integer pin, Integer employeeId, BigDecimal balance, BigDecimal absoluteLimit, Boolean activated) {
        this.iban = iban;
        this.type = type;
        this.pin = pin;
        this.employeeId = employeeId;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.activated = activated;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAbsoluteLimit() {
        return absoluteLimit;
    }

    public void setAbsoluteLimit(BigDecimal absoluteLimit) {
        this.absoluteLimit = absoluteLimit;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
