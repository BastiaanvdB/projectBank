package io.swagger.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.api.UsersApiController;
import io.swagger.model.entity.Account;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UsersApiController.class)
public class UserControllerTest {

    // Mock mvc and contect
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    // Mock jwt and mapper
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private AuthenticationManager authenticationManagerBean;
    @Autowired
    private ObjectMapper mapper;

    // Mock service and repository's
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



    // ** TESTS FOR CREATE USER
    // -- Authorization
    @Test
    void createUserWithoutRoleAuthorized() throws Exception {
        when(accountService.createAccount(any(Account.class))).thenReturn(new Account());
        mockMvc.perform(post("/users")
                        .content("{}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status()
                        .isCreated());
    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void createUserShouldReturnStatusCreatedAndNewUser() throws Exception {

    }



    // ** TESTS FOR GET ALL USERS
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void getAllUsersWithRoleUserWillReturnUnauthorized() throws Exception {

        when(userService.getAll(0, 10)).thenReturn(List.of(new User()));

        this.mockMvc.perform(get("/users?offset=0&limit=10"))
                .andDo(print()).andExpect(status().isForbidden());
    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getAllUsersShouldReturnStatusOkAndAccount() throws Exception {

        when(userService.getAll(0, 10)).thenReturn(List.of(new User(1, "Bram", "Terlouw",
                "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl",
                new ArrayList<>(List.of(Role.ROLE_USER)), "0235412412", new BigDecimal(200),
                new BigDecimal(100), true, "BramTest")));

        this.mockMvc.perform(get("/users?offset=0&limit=10"))
                .andDo(print()).andExpect(status().isOk());
    }



    // ** TESTS FOR GET ONE USER
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void getOneUserFromOtherUserWithRoleUserWillReturnUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getOneUserByUserIdShouldReturnStatusOkAndAccount() throws Exception {

        when(userService.getOne(any())).thenReturn(new User(1, "Bram", "Terlouw",
                "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl",
                new ArrayList<>(List.of(Role.ROLE_USER)), "0235412412", new BigDecimal(200),
                new BigDecimal(100), true, "BramTest"));

        this.mockMvc.perform(get("/users/2"))
                .andDo(print())
                .andExpect(status()
                        .isOk())
                .andExpect(jsonPath("$[0].Firstname")
                        .value("Bram"));
    }

    // -- User id not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void getOneUserByUserIdWithNonExistingUserIdShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR SET USER PASSWORD
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setPasswordFromOtherUserWithRoleUserWillReturnUnauthorized() throws Exception {

    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetPasswordShouldReturnStatusOk() throws Exception {

        this.mockMvc.perform(put("/users/1/password"))
                .andDo(print())
                .andExpect(status()
                        .isForbidden());
    }

    // -- User id not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetPasswordWithNonExistingUserIdShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR SET USER ROLE
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setRoleWithRoleUserWillReturnUnauthorized() throws Exception {

        this.mockMvc.perform(put("/users/1/role"))
                .andDo(print())
                .andExpect(status()
                        .isForbidden());
    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetRoleShouldReturnStatusOkAndReturnNewRole() throws Exception {

        this.mockMvc.perform(put("/users/1/role"))
                .andDo(print())
                .andExpect(status()
                        .isOk());
    }

    // -- User id not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetRoleWithNonExistingUserIdShouldReturnStatusNotFound() throws Exception {

    }



    // ** TESTS FOR SET USER STATUS
    // -- Authorization
    @Test
    @WithMockUser(username = "bram@live.nl", password = "BramTest", roles = "USER")
    void setStatusWithRoleUserWillReturnUnauthorized() throws Exception {

        this.mockMvc.perform(put("/users/1/activation"))
                .andDo(print())
                .andExpect(status()
                        .isForbidden());
    }

    // -- Success and return values
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetStatusShouldReturnStatusOkAndReturnNewStatus() throws Exception {

        this.mockMvc.perform(put("/users/1/activation"))
                .andDo(print())
                .andExpect(status()
                        .isOk());
    }

    // -- User id not found
    @Test
    @WithMockUser(username = "mark@live.nl", password = "MarkTest", roles = "EMPLOYEE")
    void SetStatusWithNonExistingUserIdShouldReturnStatusNotFound() throws Exception {

    }
}
