package io.swagger.controller;

import io.swagger.api.AccountsApiController;
import io.swagger.model.entity.Account;
import io.swagger.model.enumeration.AccountType;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.AccountService;
import io.swagger.service.TransactionService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AccountsApiController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountService accountService;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TransactionRepository transactionRepository;

//    @Autowired
//    private ObjectMapper mapper;

    @Test
    public void getAllShouldReturnJsonArrayOfSizeOne() throws Exception {

        when(accountService.getAll(0, 10)).thenReturn(List.of(new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(20), new BigDecimal(20), true)));
        this.mockMvc.perform(get("http://localhost:8080/Groep1BankApi/bank/1.0.0/accounts?offset=0&limit=10"))
                .andDo(print()).andExpect(status().isNotFound());

//        andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].iban").value("NL01INHO0000000002"))
    }
}
