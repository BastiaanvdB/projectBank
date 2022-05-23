package io.swagger.service;

import io.swagger.model.entity.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


    // Get queries
    public List<Account> getAll() {
        return (List<Account>) accountRepository.findAll();
    }

    public Account getOneByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    public Account getLastAccount() {
        return accountRepository.findLastAccountEntry();
    }



    // Post queries
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }



    // Put queries
    public void updateLimit(Account account) {
        accountRepository.updateLimit(account.getAbsoluteLimit(), account.getIban());
    }

    public void updatePin(Account account) {
        accountRepository.updatePin(account.getPin(), account.getIban());
    }

    public void updateStatus(Account account) {
        if (account.getActivated()) {
            accountRepository.updateStatus(false, account.getIban());
        } else {
            accountRepository.updateStatus(account.getActivated(), account.getIban());
        }
    }
}