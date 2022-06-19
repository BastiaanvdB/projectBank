package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.model.exception.InvalidPincodeException;
import io.swagger.model.exception.UserNotFoundException;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Swagger2SpringBoot.class, AccountService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AccountService accountService;
    private Account generatedAccount;
    private AccountDTO requestGenerateAccount;
    private Authentication auth;

    @Before
    public void setupMock() {
        generatedAccount = new Account("NL01INHO0000000003", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(-2000), true);
        requestGenerateAccount = new AccountDTO();
        requestGenerateAccount.setType(generatedAccount.getType());
        requestGenerateAccount.setUserId(2);
    }

    @Test
    public void canSaveAndReturnAccount() {
        given(accountRepository.save(generatedAccount)).willReturn(generatedAccount);
        Account savedAccount = accountRepository.save(generatedAccount);
        assertNotNull(savedAccount);
    }

    @Test
    public void canGetAccountByIbanShouldReturnOneAccount() throws InvalidIbanException, AccountNotFoundException {
        given(accountRepository.findAccountByIban(generatedAccount.getIban())).willReturn(generatedAccount);
        Account account = accountService.getOneByIban(generatedAccount.getIban());
        assertNotNull(account);
    }

    @Test
    public void getOneAccountNotFoundShouldThrowAccountNotFoundException() {
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
           accountService.getOneByIban("NL01INHO0000000009");
        });
    }
    @Test
    public void canGetAccountByIbanWithShortIbanShouldReturnInvalidIbanException() {
        Assertions.assertThrows(InvalidIbanException.class, () -> {
            accountService.getOneByIban("NL01INHO000000000");
        });
    }

    @Test
    public void canGetAccountByIbanWithWrongCountryShouldReturnInvalidIbanException(){
        Assertions.assertThrows(InvalidIbanException.class, () -> {
            accountService.getOneByIban("NR01INHO0000000003");
        });
    }

    @Test
    public void canGetAccountByIbanWithLetterInIdentifierShouldReturnInvalidIbanException() {
        Assertions.assertThrows(InvalidIbanException.class, () -> {
            accountService.getOneByIban("NL01INHO00000000e3");
        });
    }
    @Test
    public void canGetAccountByIbanWithWrongPrefixShouldReturnInvalidIbanException() {
        Assertions.assertThrows(InvalidIbanException.class, () -> {
            accountService.getOneByIban("NL01INQO000000000");
        });
    }


    @Test
    public void canGetAllAccountsByFirstnameShouldReturnListOfAccounts() {
        given(accountRepository.findAllByFirstname(PageRequest.of(0, 10), "Bram")).willReturn(Arrays.asList(generatedAccount, generatedAccount));
        List<Account> accounts = accountService.getAllByFirstname("Bram", 0, 10);
        assertNotNull(accounts);
    }
    @Test
    public void canGetAllAccountsByFirstnameAndLastnameShouldReturnListOfAccounts() {
        given(accountRepository.findAllByFirstAndLastname(PageRequest.of(0, 10), "Bram", "Terlouw")).willReturn(Arrays.asList(generatedAccount, generatedAccount));
        List<Account> accounts = accountService.getAllByFirstAndLastname("Bram", "Terlouw", 0, 10);
        assertNotNull(accounts);
    }

    @Test
    public void canGetAllAccountsByUseridShouldReturnListOfAccounts() {
        given(accountRepository.findAllByUserid(2)).willReturn(Arrays.asList(generatedAccount, generatedAccount));
        List<Account> accounts = accountService.getAllByUserId(2);
        assertNotNull(accounts);
    }
}
