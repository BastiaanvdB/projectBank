package io.swagger.service;

import io.swagger.model.entity.Account;
import io.swagger.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;


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
        return  accountRepository.findAllByUserid(userId);
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