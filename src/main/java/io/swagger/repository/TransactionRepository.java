package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query(value = "SELECT t FROM Transaction t WHERE :query")
    List<Transaction> getAll(@Param("query") String query, PageRequest of);
}
