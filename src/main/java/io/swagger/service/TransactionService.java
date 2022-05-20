package io.swagger.service;

import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.entity.Transaction;
import io.swagger.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public Transaction getAll() {return transactionRepository.getAllTransactions();}

}
