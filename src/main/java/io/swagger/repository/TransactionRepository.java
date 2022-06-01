package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, TransactionRepositoryCustom {
    @Query(value = "SELECT t FROM Transaction t WHERE t.ibanFrom = :iban AND DATE('t.iat') = NOW()", nativeQuery = true)
    List<Transaction> getAllFromToday(@Param("iban") String iban);

}
