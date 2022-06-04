package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.threeten.bp.LocalDate;

import java.util.List;

public interface TransactionRepositoryCustom {
    List<Transaction> findAllCustom(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibANTo, String balanceOperator, String balance, Integer offset, Integer limit);
}
