package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.AccountsApiController;
import io.swagger.model.entity.Account;
import io.swagger.model.enumeration.AccountType;
import io.swagger.service.AccountService;
import io.swagger.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
//@RunWith(SpringRunner.class)
@WebMvcTest(AccountsApiController.class)
public class AccountControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AccountService accountService;

    @Autowired private ObjectMapper mapper;

    @Test
    void getAllShouldReturnJsonArrayOfSizeOne() throws Exception {
        when(accountService.getAll(0, 10)).thenReturn(Arrays.asList(new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(20), new BigDecimal(20), true)));
        this.mockMvc.perform(get("/accounts"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].iban").value("NL01INHO0000000002"));
    }

//    @Test
//    public void givenAccounts_whenGetAllShouldReturnJsonArray() throws Exception {
//        List<Account> allAccounts = Arrays.asList(new Account("NL01INHO0000000002", AccountType.CURRENT, "1234", 2, new BigDecimal(20), new BigDecimal(20), true));
//        given(accountService.getAll(0, 10)).willReturn(allAccounts);
//        mockMvc.perform(get("/accounts"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].iban").value("NL01INHO0000000002"));
//    }

//    @Test
//    public void shouldFetchAllAccounts() throws Exception {
//
//        given(accountService.getAll(0, 10)).willReturn(accounts);
//
//        this.mockMvc.perform(get("/accounts"))
//                .andExpect(status().isOk());
//
//    }
}
