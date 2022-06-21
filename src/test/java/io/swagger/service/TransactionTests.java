package io.swagger.service;

import io.jsonwebtoken.Jwts;
import io.swagger.Swagger2SpringBoot;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.enumeration.AccountType;
import io.swagger.repository.TransactionRepository;
import io.swagger.security.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Swagger2SpringBoot.class, TransactionService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TransactionTests {
     private final String userToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJicmFtQGxpdmUubmwiLCJhdXRoIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY1NTMwNzA5MCwiZXhwIjoxNjU2MTcxMDkwfQ.RLQ19uw6HYVY0JE99V_jDkLVtwsFnZw7DR54EMgqfBI";


    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private Transaction generatedTransaction;
    private Account generatedAccount;
    @InjectMocks
    private TransactionService service;
    private List<Transaction> list;

    @Before
    public void before() throws Exception {
        this.generatedTransaction = new Transaction(11, "NL01INHO0000000001", "NL01INHO0000000002", new BigDecimal("100.01"), 2, Timestamp.from(Instant.now()));
        this.generatedAccount = new Account("NL01INHO0000000003", AccountType.CURRENT, "1234", 2, new BigDecimal(200), new BigDecimal(-2000), true);
    }

    @Test
    public void repositoryCanSaveAndReturnTransaction() {
        given(transactionRepository.save(this.generatedTransaction)).willReturn(generatedTransaction);
        Transaction saved = transactionRepository.save(this.generatedTransaction);
        assertNotNull(saved);
    }


    /**
     * Method: getAllFromAccount(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit)
     */
    @Test
    public void testGetAllFromAccount() throws Exception {
//        given(service.getAllFromAccount(null,null,"NL01INHO0000000002", null,null,null,null,null, userToken)).willReturn(list);
//        List<Transaction> listFromToAccount = this.service.getAllFromAccount(null,null,"NL01INHO0000000002", null,null,null,null,null, userToken);
//        assertNotNull(listFromToAccount);
    }

    /**
     * Method: createTransaction(TransactionDTO body, HttpServletRequest request)
     */
    @Test
    public void testCreateTransaction() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: deposit(DepositDTO body, String IBAN, HttpServletRequest request)
     */
    @Test
    public void testDeposit() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: withdraw(String IBAN, WithdrawDTO body, HttpServletRequest request)
     */
    @Test
    public void testWithdraw() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: saveTransaction(Transaction transaction)
     */
    @Test
    public void testSaveTransaction() throws Exception {
        given(service.saveTransaction(this.generatedTransaction)).willReturn(this.generatedTransaction);
        Transaction saved = service.saveTransaction(this.generatedTransaction);
        assertNotNull(saved);
    }

    /**
     * Method: getAll(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit, HttpServletRequest request)
     */
    @Test
    public void testGetAll() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getAllFromTodaySUM(String iban)
     */
    @Test
    public void testGetAllFromTodaySUM() throws Exception {
//TODO: Test goes here...
    }


    /**
     * Method: getAll(Integer offset, Integer limit)
     */
    @Test
    public void testGetAllForOffsetLimit() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: getAll(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit)
     */
    @Test
    public void testGetAllForStartDateEndDateIbanFromIbanToBalanceOperatorBalanceOffsetLimit() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: isPinCorrect(String pin, Account accFrom)
     */
    @Test
    public void testIsPinCorrect() throws Exception {
//TODO: Test goes here...


    }

    /**
     * Method: getUserFromToken(HttpServletRequest request)
     */
    @Test
    public void testGetUserFromToken() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: isBankAccount(String iban)
     */
    @Test
    public void testIsBankAccount() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: checkBalanceForTransaction(String iban, BigDecimal amount)
     */
    @Test
    public void testCheckBalanceForTransaction() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: doTransaction(Transaction transaction, User user)
     */
    @Test
    public void testDoTransaction() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: employeeTransaction(Transaction transaction, User user)
     */
    @Test
    public void testEmployeeTransaction() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: savingsTransaction(Transaction transaction, User user)
     */
    @Test
    public void testSavingsTransaction() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: normalTransaction(Transaction transaction, Account accFrom, User user)
     */
    @Test
    public void testNormalTransaction() throws Exception {
//TODO: Test goes here...

    }

    /**
     * Method: isUserOwner(User user, String iban)
     */
    @Test
    public void testIsUserOwner() throws Exception {
//TODO: Test goes here...

    }
}
