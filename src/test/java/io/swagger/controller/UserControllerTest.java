package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.UsersApiController;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
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
import java.util.ArrayList;
import java.util.Arrays;
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
@WebMvcTest(UsersApiController.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManagerBean;
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;
    @MockBean
    private AccountService accountService;
    @MockBean
    private TransactionService transactionService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TransactionRepository transactionRepository;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    public void testToSeeIfTestsWork1() throws Exception {

        when(userService.getAll(0, 10)).thenReturn(List.of(new User(1, "Bram", "Terlouw",
                "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl",
                new ArrayList<>(List.of(Role.ROLE_USER)), "0235412412", new BigDecimal(200),
                new BigDecimal(100), true, "BramTest")));

        this.mockMvc.perform(get("/users?offset=0&limit=10"))
                .andDo(print()).andExpect(status().isOk());
    }
}
