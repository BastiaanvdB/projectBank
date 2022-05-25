package io.swagger.configuration;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
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
import java.util.HashSet;
import java.util.Set;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Users
        User user = new User(1, "Bram", "Terlouw", "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl", new ArrayList<>(Arrays.asList(Role.ROLE_USER)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "BramTest");
        User employee = new User(2, "Mark", "Haantje", "Bijdorplaan 15", "Haarlem", "2015CE", "mark@live.nl", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "MarkTest");


        // Accounts
        Account curr = new Account("NLxxINHO0000000000", AccountType.CURRENT, "1234", 2, new BigDecimal(20), new BigDecimal(20), true);
        Account sav = new Account("NLxxINHO0000011129", AccountType.SAVINGS, "1255", 2, new BigDecimal(20), new BigDecimal(20), true);

        accountRepository.save(curr);
        accountRepository.save(sav);

        Set<Account> accountsUser = new HashSet<>();
        Set<Account> accountsEmployee = new HashSet<>();
        accountsUser.add(curr);
        accountsEmployee.add(sav);

        user.setAccounts(accountsUser);
        employee.setAccounts(accountsEmployee);

        userService.add(user);
        userService.addEmployeeFromSeeder(employee);
    }
}
