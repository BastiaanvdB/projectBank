package io.swagger.model.entity;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Account {

    @Id @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String iban;
    private AccountResponseDTO.TypeEnum type;
    private Integer pin;
    private Integer userId;
    private Integer employeeId;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private Boolean activated;

    public Account() {
    }

    public Account(String iban, AccountResponseDTO.TypeEnum type, Integer pin, Integer userId, Integer employeeId, BigDecimal balance, BigDecimal absoluteLimit, Boolean activated) {
        this.iban = iban;
        this.type = type;
        this.pin = pin;
        this.userId = userId;
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

    public AccountResponseDTO.TypeEnum getType() {
        return type;
    }

    public void setType(AccountResponseDTO.TypeEnum type) {
        this.type = type;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
