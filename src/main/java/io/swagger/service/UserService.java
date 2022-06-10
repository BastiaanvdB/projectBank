package io.swagger.service;

import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.UserNotFoundException;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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

    public List<User> getAll(Integer offset, Integer limit) {
        return userRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public User getOne(int id) {
        Optional userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return (User) userOptional.get();
        } else {
            return null;
        }
    }

    public String login(String email, String password) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = findByEmail(email);
        if (user.getActivated()) {
            return jwtTokenProvider.createToken(email, user.getRoles());
        } else {
            return null;
        }
    }

    public String EditUserAndToken(User user) {
        userRepository.save(user);
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {
            return null;
        }
    }


    public void changePassword(User user, String newPassword, String oldPassword, boolean force) {


        // bypasses if admin wants to change password from different user
        if (!force) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), oldPassword));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public User signup(User user) {


        user.setTransactionLimit(new BigDecimal(1000));
        user.setDayLimit(new BigDecimal(200));
        user.setRoles(new ArrayList<>(List.of(Role.ROLE_USER)));
        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        return user;
    }

    public void addFromSeeder(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void addAccountToUser(User user, Account account) throws UserNotFoundException {
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        user.setAccounts(new HashSet<>(List.of(account)));
        userRepository.save(user);
    }


    // Replace the old row with new row
    public User put(User user) {
        userRepository.save(user);
        return user;
    }
}
