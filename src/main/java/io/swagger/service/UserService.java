package io.swagger.service;

import io.swagger.model.DTO.UserActivationDTO;
import io.swagger.model.DTO.UserDTO;
import io.swagger.model.DTO.UserPasswordDTO;
import io.swagger.model.DTO.UserRoleDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.InvalidEmailException;
import io.swagger.model.exception.InvalidRoleException;
import io.swagger.model.exception.PasswordRequirementsException;
import io.swagger.model.exception.UserNotFoundException;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

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


    private static final BigDecimal DEFAULT_DAY_LIMIT = new BigDecimal(200);
    private static final BigDecimal DEFAULT_TRANSACTION_LIMIT = new BigDecimal(1000);
    private static final ArrayList<Role> DEFAULT_ROLE = new ArrayList<>(List.of(Role.ROLE_USER));


    public List<User> getAll(Integer offset, Integer limit) {
        return userRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public User getOne(int id) throws UserNotFoundException {
        Optional userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            return (User) userOptional.get();
        } else {
            throw new UserNotFoundException("User not found");
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

    public String EditUserAndToken(int userid, User user, UserDTO newUserDetails) throws UserNotFoundException, InvalidEmailException {

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            user = this.getOne(userid);
        }

        //check if new email already has been taken
        User checkUser = this.findByEmail(newUserDetails.getEmail());

        //check if its not the same user
        if (checkUser != null && checkUser.getId() != user.getId()) {
            throw new InvalidEmailException("Email already has been used!");
        }

        user.setFirstname(newUserDetails.getFirstname());
        user.setLastname(newUserDetails.getLastname());
        user.setEmail(newUserDetails.getEmail());
        user.setPhone(newUserDetails.getPhone());
        user.setAddress(newUserDetails.getAddress());
        user.setPostalCode(newUserDetails.getPostalCode());
        user.setCity(newUserDetails.getCity());
        user.setTransactionLimit(newUserDetails.getTransactionLimit());
        user.setDayLimit(newUserDetails.getDayLimit());

        this.put(user);
        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    public User findByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception ex) {
            return null;
        }
    }


    public void changePassword(int userid, User user, UserPasswordDTO passwordDTO) throws PasswordRequirementsException, UserNotFoundException {
        boolean force = false;

        if (passwordDTO.getNewPassword().chars().filter((s) -> Character.isUpperCase(s)).count() < 2 || passwordDTO.getNewPassword().length() < 6) {
            throw new PasswordRequirementsException("New password doesnt meet security requirements!");
        }

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE) && user.getId() != userid) {
            user = this.getOne(userid);
            force = true;
        }

        // bypasses if admin wants to change password from different user
        if (!force) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), passwordDTO.getOldPassword()));
        }

        user.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        userRepository.save(user);
    }

    public User signup(User user) {


        user.setTransactionLimit(DEFAULT_TRANSACTION_LIMIT);
        user.setDayLimit(DEFAULT_DAY_LIMIT);
        user.setRoles(DEFAULT_ROLE);
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

    public void changeUserRoles(int userid, UserRoleDTO newRoles) throws UserNotFoundException, InvalidRoleException {

        List<Role> roles = new ArrayList<>();
        try {
            for (Integer r : newRoles.getRoles()) {
                roles.add(Role.values()[r]);
            }
        } catch (Exception e) {
            throw new InvalidRoleException("Enter a valid role!");
        }

        User user = this.getOne(userid);
        user.setRoles(roles);
        this.put(user);
    }

    public void changeUserStatus(int userid, UserActivationDTO activationDTO) throws UserNotFoundException {
        User user = this.getOne(userid);
        user.setActivated(activationDTO.isActivated());
        this.put(user);
    }

    // Replace the old row with new row
    public User put(User user) {
        userRepository.save(user);
        return user;
    }
}
