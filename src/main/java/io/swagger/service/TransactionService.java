package io.swagger.service;

import io.swagger.model.entity.Transaction;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public List<Transaction> getAll(Integer offset, Integer limit) { return transactionRepository.findAll(PageRequest.of(offset, limit)).getContent(); }

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAll(String query, Integer offset, Integer limit) {
        return transactionRepository.getAll(PageRequest.of(offset, limit), query);
    }

    public List<Transaction> getAllFromToday(String iban) {
//        Timestamp today = new Timestamp(new Date().getTime());
        return transactionRepository.getAllFromToday(iban);
    }
}
