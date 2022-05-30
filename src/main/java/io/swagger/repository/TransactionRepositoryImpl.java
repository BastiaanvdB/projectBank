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
public class TransactionRepositoryImpl implements TransactionRepositoryCustom{

    EntityManager em;

    public TransactionRepositoryImpl() {
        this.em = new EntityManager() {
            @Override
            public void persist(Object o) {

            }

            @Override
            public <T> T merge(T t) {
                return null;
            }

            @Override
            public void remove(Object o) {

            }

            @Override
            public <T> T find(Class<T> aClass, Object o) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, Map<String, Object> map) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType) {
                return null;
            }

            @Override
            public <T> T find(Class<T> aClass, Object o, LockModeType lockModeType, Map<String, Object> map) {
                return null;
            }

            @Override
            public <T> T getReference(Class<T> aClass, Object o) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public void setFlushMode(FlushModeType flushModeType) {

            }

            @Override
            public FlushModeType getFlushMode() {
                return null;
            }

            @Override
            public void lock(Object o, LockModeType lockModeType) {

            }

            @Override
            public void lock(Object o, LockModeType lockModeType, Map<String, Object> map) {

            }

            @Override
            public void refresh(Object o) {

            }

            @Override
            public void refresh(Object o, Map<String, Object> map) {

            }

            @Override
            public void refresh(Object o, LockModeType lockModeType) {

            }

            @Override
            public void refresh(Object o, LockModeType lockModeType, Map<String, Object> map) {

            }

            @Override
            public void clear() {

            }

            @Override
            public void detach(Object o) {

            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public LockModeType getLockMode(Object o) {
                return null;
            }

            @Override
            public void setProperty(String s, Object o) {

            }

            @Override
            public Map<String, Object> getProperties() {
                return null;
            }

            @Override
            public Query createQuery(String s) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery) {
                return null;
            }

            @Override
            public Query createQuery(CriteriaUpdate criteriaUpdate) {
                return null;
            }

            @Override
            public Query createQuery(CriteriaDelete criteriaDelete) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createQuery(String s, Class<T> aClass) {
                return null;
            }

            @Override
            public Query createNamedQuery(String s) {
                return null;
            }

            @Override
            public <T> TypedQuery<T> createNamedQuery(String s, Class<T> aClass) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s, Class aClass) {
                return null;
            }

            @Override
            public Query createNativeQuery(String s, String s1) {
                return null;
            }

            @Override
            public StoredProcedureQuery createNamedStoredProcedureQuery(String s) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s, Class... classes) {
                return null;
            }

            @Override
            public StoredProcedureQuery createStoredProcedureQuery(String s, String... strings) {
                return null;
            }

            @Override
            public void joinTransaction() {

            }

            @Override
            public boolean isJoinedToTransaction() {
                return false;
            }

            @Override
            public <T> T unwrap(Class<T> aClass) {
                return null;
            }

            @Override
            public Object getDelegate() {
                return null;
            }

            @Override
            public void close() {

            }

            @Override
            public boolean isOpen() {
                return false;
            }

            @Override
            public EntityTransaction getTransaction() {
                return null;
            }

            @Override
            public EntityManagerFactory getEntityManagerFactory() {
                return null;
            }

            @Override
            public CriteriaBuilder getCriteriaBuilder() {
                return null;
            }

            @Override
            public Metamodel getMetamodel() {
                return null;
            }

            @Override
            public <T> EntityGraph<T> createEntityGraph(Class<T> aClass) {
                return null;
            }

            @Override
            public EntityGraph<?> createEntityGraph(String s) {
                return null;
            }

            @Override
            public EntityGraph<?> getEntityGraph(String s) {
                return null;
            }

            @Override
            public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> aClass) {
                return null;
            }
        };
    }

    @Override
    public List<Transaction> findAllCustom(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit) {
        CriteriaBuilder  cb = em.getCriteriaBuilder();
        CriteriaQuery<Transaction> cq = cb.createQuery(Transaction.class);

        Root<Transaction> trans = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<>();

        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 50;
        }
        if (ibanFrom != null){
            predicates.add(cb.equal(trans.get("ibanFrom"), ibanFrom));
        }
        if (ibanTo != null) {
            predicates.add(cb.equal(trans.get("ibanTo" ), ibanTo));
        }
        //todo: check how i can implement the balance operator
        if (balance != null) {
            if (balanceOperator == null) {
                balanceOperator = ("=");
            }
            predicates.add(cb.equal(trans.get("balance"), balance));
        }
        // todo: check how timings work with this
        if (startDate != null && endDate != null) {
            Timestamp tsS = Timestamp.valueOf(String.valueOf(startDate.atStartOfDay()));
            Timestamp tsE = Timestamp.valueOf(String.valueOf(endDate.atStartOfDay()));

        } else if (startDate != null) {
            Timestamp ts = Timestamp.valueOf(String.valueOf(startDate.atStartOfDay()));

        } else if (endDate != null) {
            Timestamp ts = Timestamp.valueOf(String.valueOf(endDate.atStartOfDay()));

        }
        

        cq.where(predicates.toArray(new Predicate[0]));
        return em.createQuery(cq).getResultList();
    }
}
