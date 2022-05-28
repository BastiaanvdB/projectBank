package io.swagger.model;

import io.swagger.model.entity.Transaction;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class TransactionTest {

    @Test
    public void newAccountShouldNotBeNull() {
        Transaction transaction = new Transaction();
        Assertions.assertNotNull(transaction);
    }
}
