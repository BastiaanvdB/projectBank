package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;
import java.util.List;
import java.util.Map;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String>, TransactionRepositoryCustom {
    @Query(value = "SELECT t FROM Transaction t WHERE t.ibanFrom = :iban AND DATE('t.iat') = NOW()", nativeQuery = true)
    List<Transaction> getAllFromToday(@Param("iban") String iban);

}
