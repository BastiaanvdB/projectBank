package io.swagger.service;

import io.swagger.Swagger2SpringBoot;
import io.swagger.model.DTO.UserFilterDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.UserNotFoundException;
import io.swagger.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Swagger2SpringBoot.class, UserService.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UsersTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;
    private User generatedEmployee;
    private User generatedUser;
//    private UserDTO requestGenerateUser;

    @Before
    public void setupMock(){
        this.generatedUser = new User(2, "Bram", "Terlouw", "Bijdorplaan 15", "Haarlem", "2015CE", "bram@live.nl", new ArrayList<>(List.of(Role.ROLE_USER)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "BramTest");
        this.generatedUser = new User(3, "Mark", "Haantje", "Bijdorplaan 15", "Haarlem", "2015CE", "mark@bbcbank.nl", new ArrayList<>(Arrays.asList(Role.ROLE_EMPLOYEE, Role.ROLE_USER)), "0235412412", new BigDecimal(200), new BigDecimal(100), true, "MarkTest");
    }

    @Test
    public void canSaveAndReturnUser() {
        given(userRepository.save(generatedUser)).willReturn(generatedUser);
        User savedUser = userRepository.save(generatedUser);
        assertNotNull(savedUser);
    }

    @Test
    public void canGetUserByUseridShouldReturnOneUser() throws UserNotFoundException {
        given(userRepository.findById(generatedUser.getId())).willReturn(Optional.ofNullable(generatedUser));
        User user = userService.getOne(generatedUser.getId());
        assertNotNull(user);
    }

    @Test
    public void canGetAllUsersOnFilterShouldReturnListOfUsers() {
        List<User> users = Arrays.asList(generatedUser, generatedEmployee);
        Page<User> pagedResponse = new PageImpl(users);
        given(userRepository.findAll(PageRequest.of(0, 10))).willReturn(pagedResponse);
        List<User> userList = userService.getAllWithFilter(new UserFilterDTO(0, 10, null, null, false, true, false, true));
        assertNull(userList);
    }


}
