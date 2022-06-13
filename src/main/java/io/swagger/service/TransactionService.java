package io.swagger.service;

import io.swagger.model.entity.Transaction;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.TransactionRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionRepositoryImpl transactionRepoImpl;

    public List<Transaction> getAll(Integer offset, Integer limit) {
        return transactionRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAll(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit) {
        return transactionRepoImpl.findAllCustom(startDate, endDate, ibanFrom, ibanTo, balanceOperator, balance, offset, limit);
    }

    public BigDecimal getAllFromTodaySUM(String iban) {
        BigDecimal var = transactionRepository.getAllFromTodaySUM(iban);
        if (var == null) {var = BigDecimal.ZERO;}
        return var;
    }
}
