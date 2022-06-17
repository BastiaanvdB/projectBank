package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Swagger2SpringBoot.class, AccountService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;
    private Account generatedAccount;
    private AccountDTO requestGenerateAccount;

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
    public void canGetAllAccountsShouldReturnListOfAccounts() {
//        given(accountRepository.findAll(PageRequest.of(0, 10)).getContent()).willReturn(Arrays.asList(generatedAccount, generatedAccount));
//        List<Account> accounts = accountService.getAll(0, 10);
//        assertNotNull(accounts);
    }

    @Test
    public void canGetAllAccountsByUseridShouldReturnListOfAccounts() {
        given(accountRepository.findAllByUserid(2)).willReturn(Arrays.asList(generatedAccount, generatedAccount));
        List<Account> accounts = accountService.getAllByUserId(2);
        assertNotNull(accounts);
    }
}
