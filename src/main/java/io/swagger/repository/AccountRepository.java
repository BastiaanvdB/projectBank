package io.swagger.repository;

import io.swagger.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, String> {

    @Query(value = "SELECT * FROM Account a WHERE a.iban = ?1", nativeQuery = true)
    public Account findAccountByIban(String iban);
}
