package io.swagger.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    @Column(name="id")
    private Integer id;
    @Column(name="ibanFrom")
    private String ibanFrom;
    @Column(name="ibanTo")
    private String ibanTo;
    @Column(name="amount")
    private BigDecimal amount;
    @Column(name="issuedBy")
    private Integer issuedBy;
    @CreationTimestamp
    @Column(name="iat")
    private Timestamp iat;
}
