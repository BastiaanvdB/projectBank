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

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account getOneByIban(String iban) {
        return accountRepository.findAccountByIban(iban);
    }

    public Account getLastAccount() {
        return accountRepository.findLastAccountEntry();
    }
}
