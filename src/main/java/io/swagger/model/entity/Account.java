package io.swagger.model.entity;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String iban;
    private AccountResponseDTO.TypeEnum type;
    private Integer pin;
    private Integer userId;
    private Integer employeeId;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private Boolean activated;

}
