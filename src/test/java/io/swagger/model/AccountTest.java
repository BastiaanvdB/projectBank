package io.swagger.model;

import io.swagger.model.entity.Account;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTest {

    @Test
    public void newAccountShouldNotBeNull() {
        Account account = new Account();
        Assertions.assertNotNull(account);
    }
}
