package io.swagger.configuration;

import io.swagger.model.ResponseDTO.AccountResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userRepository.save(new User(1, "Bram", "Terlouw", "Address", "Alkmaar", "postalcode", "email", 1, "phonenumber", new BigDecimal(200), new BigDecimal(100), true));
        accountRepository.save(new Account("iban", AccountResponseDTO.TypeEnum.CURRENT, 1234, 1, 2, new BigDecimal(20), new BigDecimal(20), true));
    }
}
