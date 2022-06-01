package io.swagger.configuration;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
import io.swagger.repository.AccountRepository;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
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

    @Autowired
    private TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Users
        User user = new User(1, "Bram", "Terlouw", "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl", new ArrayList<>(Arrays.asList(Role.ROLE_USER)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "BramTest");
        User employee = new User(2, "Mark", "Haantje", "Bijdorplaan 15", "Haarlem", "2015CE", "mark@live.nl", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "MarkTest");


        // Accounts
        Account bank = new Account("NL01INHO0000000001", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account curr1 = new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account curr2 = new Account("NL01INHO0000000003", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account curr3 = new Account("NL01INHO0000000004", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account sav1 = new Account("NL01INHO0999999996", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account sav2 = new Account("NL01INHO0999999997", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(20), true);
        Account sav3 = new Account("NL01INHO0999999998", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(20), true);

        // Transactions
        Transaction trans = new Transaction(1, "NL01INHO0000000001", "NL01INHO0000000003", new BigDecimal(200), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans2 = new Transaction(2, "NL01INHO0000000001", "NL01INHO0000000002", new BigDecimal(100), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans3 = new Transaction(3, "NL01INHO0000000001", "NL01INHO0000000004", new BigDecimal(150), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans4 = new Transaction(4, "NL01INHO0000000002", "NL01INHO0000000001", new BigDecimal(112), 1, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));

        accountRepository.save(bank);
        accountRepository.save(curr1);
        accountRepository.save(curr2);
        accountRepository.save(curr3);
        accountRepository.save(sav1);
        accountRepository.save(sav2);
        accountRepository.save(sav3);

        Set<Account> accountsUser = new HashSet<>();
        Set<Account> accountsEmployee = new HashSet<>();
        accountsUser.add(curr1);
        accountsUser.add(sav2);
        accountsUser.add(sav3);
        accountsEmployee.add(curr2);
        accountsEmployee.add(curr3);
        accountsEmployee.add(sav1);
        accountsEmployee.add(bank);

        user.setAccounts(accountsUser);
        employee.setAccounts(accountsEmployee);
        try {
            userService.addFromSeeder(user);
            userService.addFromSeeder(employee);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enter all user details!");
        }

        transactionService.createTransaction(trans);
        transactionService.createTransaction(trans2);
    }
}
