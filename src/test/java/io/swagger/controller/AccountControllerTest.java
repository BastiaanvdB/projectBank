package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.AccountsApiController;
import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.enumeration.AccountType;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsApiController.class)
public class AccountControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManagerBean;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    public void testToSeeIfTestsWork1() throws Exception {

        when(accountService.getAll(0, 10)).thenReturn(List.of(new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(20), new BigDecimal(20), true)));
        this.mockMvc.perform(get("/accounts?offset=0&limit=10"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL01INHO0000000002"));
    }

    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void testToSeeIfTestsWork2() throws Exception {
        Account account = new Account();
        account.setIban("NL01INHO0000000002");
        account.setBalance(new BigDecimal(20));
        account.setType(AccountType.CURRENT);
        account.setPin("1234");
        account.setEmployeeId(2);
        account.setAbsoluteLimit(new BigDecimal(20));
        account.setActivated(true);
        AccountDTO dto = new AccountDTO();
        when(accountService.createAccount(any(Account.class))).thenReturn(account);
        mockMvc.perform(post("/accounts")
                        .content(mapper.writeValueAsString(dto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testToSeeIfTestsWork3() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(new Account());
        mockMvc.perform(post("/accounts")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }



    // ** TESTS FOR CREATE USER
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void createAccountWithRoleUserWillReturnUnauthorized() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(new Account());
        mockMvc.perform(post("/accounts")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void createUserShouldReturnStatusCreatedAndNewAccount() throws Exception {

    }



    // ** TESTS FOR GET ACCOUNT BY IBAN
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void getAccountByIbanFromOtherUserWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAccountByIbanShouldReturnStatusOkAndAccount() throws Exception {

    }

    // -- Iban not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAccountByIbanShouldWithNonExistingIbanShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR GET ALL ACCOUNTS BY USER ID
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void getAllAccountsByUserIdFromOtherUserWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAllAccountsByUserIdShouldReturnStatusOkAndAccount() throws Exception {

    }

    // -- User id not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAllAccountsByUserIdWithNonExistingUserIdShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR GET ALL ACCOUNTS
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void getAllAccountsWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAllAccountsShouldReturnStatusOkAndAccount() throws Exception {

    }



    // ** TESTS FOR SET ACCOUNT LIMIT
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setLimitWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetLimitShouldReturnStatusOkAndNewLimit() throws Exception {

    }

    // -- Iban not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void setLimitWithNonExistingIbanShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR SET ACCOUNT PIN
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setPinFromOtherUserWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void setPinShouldReturnStatusOkAndNewPin() throws Exception {

    }

    // -- Iban not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void setPinWithNonExistingIbanShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR SET ACCOUNT STATUS
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setStatusWithRoleUserUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void setStatusShouldReturnStatusOkAndNewStatus() throws Exception {

    }

    // -- Iban not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void setStatusWithNonExistingIbanShouldReturnStatusNotFound() throws Exception {

    }
}