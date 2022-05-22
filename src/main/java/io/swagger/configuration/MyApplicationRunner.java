package io.swagger.configuration;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.repository.AccountRepository;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Users
        User user = new User(1, "Bram", "Terlouw", "Address", "Alkmaar", "postalcode", "user", new ArrayList<>(Arrays.asList(Role.ROLE_USER)), "phonenumber", new BigDecimal(200), new BigDecimal(100), true, "test");
        User employee = new User(2, "Mark", "Haantje", "Address", "Alkmaar", "postalcode", "employee", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE)), "phonenumber", new BigDecimal(200), new BigDecimal(100), true, "test");
        userService.add(user);
        userService.add(employee);

        // Accounts
        accountRepository.save(new Account("NLxxINHO0000000000", AccountResponseDTO.TypeEnum.CURRENT, 1234, 1, 2, new BigDecimal(20), new BigDecimal(20), true));
        accountRepository.save(new Account("NLxxINHO0000011129", AccountResponseDTO.TypeEnum.CURRENT, 1255, 1, 2, new BigDecimal(20), new BigDecimal(20), true));
    }
}
