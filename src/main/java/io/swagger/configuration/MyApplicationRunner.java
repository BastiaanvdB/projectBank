package io.swagger.configuration;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
import io.swagger.service.AccountService;
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
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Users
        User bank = new User(1, "BBC Bank", "", "Bijdorplaan 15", "Haarlem", "2015CE", "bank@bbcbank.nl", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "BankTest");
        User user = new User(2, "Bram", "Terlouw", "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl", new ArrayList<>(Arrays.asList(Role.ROLE_USER)), "0235412412", new BigDecimal(1500), new BigDecimal(1000), true, "BramTest");
        User employee = new User(3, "Mark", "Haantje", "Bijdorplaan 15", "Haarlem", "2015CE", "mark@bbcbank.nl", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE, Role.ROLE_USER)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "MarkTest");

        // Accounts
        Account account_Bank = new Account("NL01INHO0000000001", AccountType.CURRENT, "1234", 2, new BigDecimal(20000000), new BigDecimal(0), true);
        Account account_Current1 = new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(-2000), true);
        Account account_Current2 = new Account("NL01INHO0000000003", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(0), true);
        Account account_Current3 = new Account("NL01INHO0000000004", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(0), true);
        Account account_Savings1 = new Account("NL01INHO0999999996", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(0), true);
        Account account_Savings2 = new Account("NL01INHO0999999997", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(0), true);
        Account account_Savings3 = new Account("NL01INHO0999999998", AccountType.SAVINGS, "1255", 2, new BigDecimal(200), new BigDecimal(0), true);

        // Transactions
        Transaction trans = new Transaction(1, "NL01INHO0000000001", "NL01INHO0000000002", new BigDecimal(200), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans2 = new Transaction(2, "NL01INHO0000000003", "NL01INHO0000000002", new BigDecimal(100), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans3 = new Transaction(3, "NL01INHO0000000002", "NL01INHO0000000004", new BigDecimal(99), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans4 = new Transaction(4, "NL01INHO0000000004", "NL01INHO0000000001", new BigDecimal(100), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans5 = new Transaction(5, "NL01INHO0000000002", "NL01INHO0000000001", new BigDecimal(75), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));
        Transaction trans6 = new Transaction(6, "NL01INHO0000000001", "NL01INHO0000000004", new BigDecimal(22), 2, Timestamp.from(Instant.ofEpochSecond(Instant.now().getEpochSecond())));

        accountService.createFromSeeder(account_Bank);
        accountService.createFromSeeder(account_Current1);
        accountService.createFromSeeder(account_Current2);
        accountService.createFromSeeder(account_Current3);
        accountService.createFromSeeder(account_Savings1);
        accountService.createFromSeeder(account_Savings2);
        accountService.createFromSeeder(account_Savings3);

        Set<Account> accounts_List_User = new HashSet<>();
        Set<Account> accounts_List_Employee = new HashSet<>();
        Set<Account> accounts_List_Bank = new HashSet<>();
        accounts_List_User.add(account_Current1);
        accounts_List_User.add(account_Savings2);
        accounts_List_User.add(account_Savings3);
        accounts_List_Employee.add(account_Current2);
        accounts_List_Employee.add(account_Current3);
        accounts_List_Employee.add(account_Savings1);
        accounts_List_Bank.add(account_Bank);

        bank.setAccounts(accounts_List_Bank);
        user.setAccounts(accounts_List_User);
        employee.setAccounts(accounts_List_Employee);

        try {
            userService.addFromSeeder(bank);
            userService.addFromSeeder(user);
            userService.addFromSeeder(employee);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enter all user details!");
        }

        transactionService.saveTransaction(trans);
        transactionService.saveTransaction(trans2);
        transactionService.saveTransaction(trans3);
        transactionService.saveTransaction(trans4);
        transactionService.saveTransaction(trans5);
        transactionService.saveTransaction(trans6);
    }
}
