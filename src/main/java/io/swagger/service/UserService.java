package io.swagger.service;

import io.swagger.model.DTO.UserPasswordDTO;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getOne(int id) {
        return userRepository.findById(id);
    }

    public String login(String email, String password) {
        String token = "";

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            User user = userRepository.findByEmail(email);
            token = jwtTokenProvider.createToken(email, user.getRoles());
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Invalid user credentials.");
        }

        return token;
    }

    public boolean changePassword(UserPasswordDTO newPassword, User user) {

        if (newPassword.getNewPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || newPassword.getNewPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "New password doesnt meet security requirements!");
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), newPassword.getOldPassword()));
            userRepository.changePassword(passwordEncoder.encode(newPassword.getNewPassword()), user.getEmail());

        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Current password is invalid!");
        }
        return true;
    }

    public User add(User user) {

        if (user.getFirstname().length() < 2 || user.getLastname().length() < 2 || user.getPhone().length() < 10 || user.getPostalCode().length() < 6 || user.getCity().length() < 2 || user.getAddress().length() < 2) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enter all user details!");
        }

        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Enter a correct email!");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Email already has been used!");
        }

        if (user.getPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || user.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Password doesnt meet security requirements!");
        }

        try {
            user.setTransactionLimit(new BigDecimal(1000));
            user.setDayLimit(new BigDecimal(200));
            user.setRoles(new ArrayList<>(Arrays.asList(Role.ROLE_USER)));
            user.setActivated(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Enter all user details!");
        }

        return user;
    }
}
