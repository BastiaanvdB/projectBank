package io.swagger.repository;

import io.swagger.model.entity.Account;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, String> {

    // Get by property
    @Query(value = "SELECT * FROM Account a WHERE a.iban = ?1", nativeQuery = true)
    public Account findAccountByIban(String iban);

    @Query(value = "SELECT * from Account a ORDER BY a.iban DESC LIMIT 1", nativeQuery = true)
    public Account findLastAccountEntry();

    // Get with join
    @Query(value = "Select * from ACCOUNT LEFT JOIN USER ON USER_ID=USER.ID WHERE USER.FIRSTNAME LIKE %?#{#firstname}%", nativeQuery = true)
    List<Account> findAllByFirstname(PageRequest of, @Param("firstname") String firstname);

    @Query(value = "Select * from ACCOUNT LEFT JOIN USER ON USER_ID=USER.ID WHERE USER.LASTNAME LIKE %?#{#lastname}%", nativeQuery = true)
    public List<Account> findAllByLastname(PageRequest of, @Param("lastname") String lastname);

    @Query(value = "Select * from ACCOUNT LEFT JOIN USER ON USER_ID=USER.ID WHERE USER.FIRSTNAME LIKE %?#{#firstname}% OR USER.LASTNAME LIKE %?#{#lastname}%", nativeQuery = true)
    public List<Account> findAllByFirstAndLastname(PageRequest of, @Param("firstname") String firstname, @Param("lastname") String lastname);

    // Update queries
    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.absoluteLimit = ?1 WHERE a.iban = ?2")
    public void updateLimit(BigDecimal limit, String iban);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.pin = ?1 WHERE a.iban = ?2")
    public void updatePin(String pin, String iban);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Account a SET a.activated = ?1 WHERE a.iban = ?2")
    public void updateStatus(boolean status, String iban);
}
