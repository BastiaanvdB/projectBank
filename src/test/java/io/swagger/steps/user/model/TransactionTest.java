package io.swagger.model;

import io.swagger.model.entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void newAccountShouldNotBeNull() {
        Transaction transaction = new Transaction();
        Assertions.assertNotNull(transaction);
    }
}
