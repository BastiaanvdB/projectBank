package io.swagger.repository;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TransactionRepository extends JpaRepository<Account, String> {

    @Query(value = "SELECT * FROM Transactions", nativeQuery = true)
    public Transaction getAllTransactions();
}
