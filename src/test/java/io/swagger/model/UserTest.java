package io.swagger.model;

import io.swagger.model.entity.User;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class UserTest {

    @Test
    public void newAccountShouldNotBeNull() {
        User user = new User();
        Assertions.assertNotNull(user);
    }
}
