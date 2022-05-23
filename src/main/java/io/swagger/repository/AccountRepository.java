package io.swagger.repository;

import io.swagger.model.entity.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    // Get queries
    @Query(value = "SELECT * FROM Account a WHERE a.iban = ?1", nativeQuery = true)
    public Account findAccountByIban(String iban);
    @Query(value = "SELECT * from Account a ORDER BY a.iban DESC LIMIT 1", nativeQuery = true)
    public Account findLastAccountEntry();

    @Query(value = "SELECT * FROM Account a JOIN a.userId u WHERE u.firstname = ?1", nativeQuery = true)
    List<Account> findAllByFirstname(PageRequest of, String firstname);
    @Query(value = "SELECT * FROM Account a JOIN a.userId u WHERE u.lastname = ?1", nativeQuery = true)
    public List<Account> findAllByLastname(PageRequest of, String lastname);

    // Update queries
    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.absoluteLimit = ?1 WHERE a.iban = ?2")
    public void updateLimit(BigDecimal limit, String iban);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.pin = ?1 WHERE a.iban = ?2")
    public void updatePin(int pin, String iban);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.activated = ?1 WHERE a.iban = ?2")
    public void updateStatus(boolean status, String iban);
}
