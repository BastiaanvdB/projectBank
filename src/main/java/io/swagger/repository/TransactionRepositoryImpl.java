package io.swagger.repository;

import io.swagger.model.entity.Transaction;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.Metamodel;
import java.sql.Timestamp;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
    @PersistenceContext
    private EntityManager em;
    @Override
    public List<Transaction> findAllCustom(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);

        Root<Transaction> trans = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 50;
        }
        if (ibanFrom != null) {
            predicates.add(cb.equal(trans.get("ibanFrom"), ibanFrom));
        }
        if (ibanTo != null) {
            predicates.add(cb.equal(trans.get("ibanTo"), ibanTo));
        }
        if (balance != null) {
            if (balanceOperator == null) {
                predicates.add(cb.equal(trans.get("amount"), balance));
            } else if (balanceOperator.equals(">")) {
                predicates.add(cb.greaterThan(trans.get("amount"), balance));
            } else if (balanceOperator.equals("<")) {
                predicates.add(cb.lessThan(trans.get("amount"), balance));
            }else if (balanceOperator.equals("<=")) {
                predicates.add(cb.lessThanOrEqualTo(trans.get("amount"), balance));
            }else if (balanceOperator.equals(">=")) {
                predicates.add(cb.greaterThanOrEqualTo(trans.get("amount"), balance));
            }
        }
        // todo: check how timings work with this
        if (startDate != null && endDate != null) {
            Timestamp tsS = Timestamp.valueOf(startDate + " 00:00:00");
            Timestamp tsE = Timestamp.valueOf(endDate + " 23:59:59");
            predicates.add(cb.between(trans.get("iat"), tsS, tsE));
        } else if (startDate != null) {
            Timestamp ts = Timestamp.valueOf(startDate + " 00:00:00");
            predicates.add(cb.greaterThanOrEqualTo(trans.get("iat"), ts));
        } else if (endDate != null) {
            Timestamp ts = Timestamp.valueOf(endDate + " 23:59:59");
            predicates.add(cb.lessThanOrEqualTo(trans.get("iat"), ts));
        }

        cq.where(predicates.toArray(new Predicate[0]));
         return em.createQuery(cq)
                .setFirstResult(offset) // offset of items
                .setMaxResults(limit)   // limit of items return
                .getResultList();
    }
}
