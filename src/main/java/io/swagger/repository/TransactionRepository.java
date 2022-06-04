package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, TransactionRepositoryCustom {
    @Query(value = "SELECT SUM(t.amount) FROM Transaction t WHERE t.ibanFrom = :iban AND t.iat > CURRENT_DATE()")
    BigDecimal getAllFromTodaySUM(String iban);
}
