package io.swagger.model;

import io.swagger.model.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void newAccountShouldNotBeNull() {
        User user = new User();
        Assertions.assertNotNull(user);
    }
}
