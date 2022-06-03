package io.swagger.service;

import io.swagger.model.entity.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(0);
    private static final BigDecimal DEFAULT_ACCOUNT_ABSOLUTELIMIT = new BigDecimal(20);
    private static final Boolean DEFAULT_ACCOUNT_ACTIVATION = true;


    // Get queries
    public List<Account> getAll(int offset, int limit) {
        Page<Account> result = accountRepository.findAll(PageRequest.of(offset, limit));
        return result.getContent();
    }

    public List<Account> getAllByFirstname(String firstname, int offset, int limit) {
        return accountRepository.findAllByFirstname(PageRequest.of(offset, limit), firstname);
    }

    public List<Account> getAllByLastname(String lastname, int offset, int limit) {
        return accountRepository.findAllByLastname(PageRequest.of(offset, limit), lastname);
    }

    public List<Account> getAllByUserId(int userId) {
        return accountRepository.findAllByUserid(userId);
    }

    public List<Account> getAllByFirstAndLastname(String firstname, String lastname, int offset, int limit) {
        return accountRepository.findAllByFirstAndLastname(PageRequest.of(offset, limit), firstname, lastname);
    }

    public Account getOneByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    public Account getLastAccount() {
        return accountRepository.findLastAccountEntry();
    }


    // Post queries
    public Account createAccount(Account account) {
        account.setIban(this.generateIban());
        account.setBalance(DEFAULT_ACCOUNT_BALANCE);
        account.setActivated(DEFAULT_ACCOUNT_ACTIVATION);
        account.setAbsoluteLimit(DEFAULT_ACCOUNT_ABSOLUTELIMIT);
        account.setPin(generatePincode());
        return accountRepository.save(account);
    }

    public void createFromSeeder(Account account) {
        account.setPin(passwordEncoder.encode(account.getPin()));
        accountRepository.save(account);
    }


    // Put queries
    public void updateLimit(Account account) {
        accountRepository.updateLimit(account.getAbsoluteLimit(), account.getIban());
    }

    public void updateBalance(Account account) {
        accountRepository.updateLimit(account.getAbsoluteLimit(), account.getIban());
    }

    public void updatePin(Account account) {
        accountRepository.updatePin(account.getPin(), account.getIban());
    }

    public void updateStatus(Account account) {
        accountRepository.updateStatus(account.getActivated(), account.getIban());
    }


    // **** HELPER METHODS
    private String generateIban() {

        // Get old iban and construct new iban with it
        String lastIban = this.getLastAccount().getIban();

        return constructIban(lastIban.substring(0, 9), lastIban.substring(9));
    }

    private String constructIban(String prefix, String identifier) {

        // Check if de prefix counter must be raised
        if (identifier.equals("999999999")) {

            // When identifier maxed, reset to 1 and raise prefix counter with 1
            identifier = "000000001";
            int prefixNumber = Integer.parseInt(prefix.substring(2, 4)) + 1;

            // Add 0 to prefix counter when number contains only 1 digit and add remaining prefix
            if (String.valueOf(prefixNumber).length() == 1) {
                prefix = "NL0" + String.valueOf(prefixNumber) + "INHO0";
            } else {
                prefix = "NL" + String.valueOf(prefixNumber) + "INHO0";
            }
        } else {

            // generate new identifier when identifier not maxed
            identifier = generatateIdentifier(identifier);
        }

        // Combine prefix and identifier and return
        return prefix + identifier;
    }

    private String generatateIdentifier(String identifier) {

        // Get new identifier and amount of digits
        int number = Integer.parseInt(identifier) + 1;
        int amountOfDigits = String.valueOf(number).length();

        // foreach leftover digit place, append a 0
        String newIdentifier = "";
        for (int i = amountOfDigits; i < 9; i++) {
            newIdentifier += '0';
        }

        // Add remaining number and return new identifier
        newIdentifier += number;
        return newIdentifier;
    }

    private String generatePincode() {

        // Create random pin with 4 digits
        Random rnd = new Random();
        return passwordEncoder.encode(String.format("%04d", rnd.nextInt(10000)));
    }
}