package io.swagger.repository;

import io.swagger.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

//    @Transactional
//    @Modifying
//    @Query(value = "UPDATE User a SET a.password = ?1 WHERE a.email = ?2")
//    public void changePassword(String password, String email);

    User findById(int id);
}
