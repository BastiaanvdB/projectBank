package io.swagger.service;

import io.swagger.model.DTO.*;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.*;
import io.swagger.repository.UserRepository;
import io.swagger.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @Lazy
    @Autowired
    private AccountService accountService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;


    private static final BigDecimal DEFAULT_DAY_LIMIT = new BigDecimal(200);
    private static final BigDecimal DEFAULT_TRANSACTION_LIMIT = new BigDecimal(1000);
    private static final ArrayList<Role> DEFAULT_ROLE = new ArrayList<>(List.of(Role.ROLE_USER));


    public List<User> getAllWithFilter(UserFilterDTO userFilterDTO) {

        List<User> usersFilteredNames = new ArrayList<>();
        List<User> usersFilteredStatus = new ArrayList<>();
        List<User> userFilteredHasAccount = new ArrayList<>();

        List<User> users = this.getAll(userFilterDTO.getOffset(), userFilterDTO.getLimit());

        // filters users on firstname, lastname or both
        for (User s : users) {
            if (!s.getEmail().equals("bank@bbcbank.nl")) {
                if (userFilterDTO.getFirstname() != null && userFilterDTO.getLastname() == null) {
                    if (s.getFirstname().toLowerCase().contains(userFilterDTO.getFirstname().toLowerCase())) {
                        usersFilteredNames.add(s);
                    }
                } else if (userFilterDTO.getFirstname() == null && userFilterDTO.getLastname() != null) {
                    if (s.getLastname().toLowerCase().contains(userFilterDTO.getLastname().toLowerCase())) {
                        usersFilteredNames.add(s);
                    }
                } else if (userFilterDTO.getFirstname() != null && userFilterDTO.getLastname() != null) {

                    if (s.getFirstname().toLowerCase().contains(userFilterDTO.getFirstname().toLowerCase()) && s.getLastname().toLowerCase().contains(userFilterDTO.getLastname().toLowerCase())) {
                        usersFilteredNames.add(s);
                    }
                } else {
                    usersFilteredNames.add(s);
                }
            }
        }

        // filters users on activation
        if (userFilterDTO.isActivatedFilterEnable()) {
            for (User s : usersFilteredNames) {
                if (userFilterDTO.isActivated() && Boolean.TRUE.equals(s.getActivated()) || !userFilterDTO.isActivated() && Boolean.FALSE.equals(s.getActivated())) {
                    usersFilteredStatus.add(s);
                }
            }
        } else {
            usersFilteredStatus = usersFilteredNames;
        }

        // filters users on owning a account or not
        if (userFilterDTO.isAccountFilterEnable()) {
            for (User s : usersFilteredStatus) {
                if (userFilterDTO.isHasAccount() && !accountService.getAllByUserId(s.getId()).isEmpty() && accountService.getAllByUserId(s.getId()) != null || !userFilterDTO.isHasAccount() && accountService.getAllByUserId(s.getId()).isEmpty() && accountService.getAllByUserId(s.getId()) != null) {
                    userFilteredHasAccount.add(s);
                }
            }
        } else {
            userFilteredHasAccount = usersFilteredStatus;
        }

        return userFilteredHasAccount;
    }

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

    public String login(LoginDTO loginDTO) throws InvalidAuthenticationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new InvalidAuthenticationException("Invalid user credentials!");
        }
        User user = findByEmail(loginDTO.getEmail());

        if (!user.getActivated()) {
            throw new InvalidAuthenticationException("Invalid user credentials!");
        }

        return jwtTokenProvider.createToken(user.getEmail(), user.getRoles());
    }

    public String EditUserAndToken(int userid, User user, UserDTO newUserDetails) throws UserNotFoundException, InvalidEmailException {

        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            user = this.getOne(userid);
        }

        //check if new email already has been taken
        User checkUser = this.findByEmail(newUserDetails.getEmail());

        //check if its not the same user
        if (checkUser != null && !checkUser.getId().equals(user.getId())) {
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

        if (passwordDTO.getNewPassword().chars().filter(Character::isUpperCase).count() < 2 || passwordDTO.getNewPassword().length() < 6) {
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

    public User signup(User user) throws InvalidEmailException, PasswordRequirementsException {

        if (this.findByEmail(user.getEmail()) != null) {
            throw new InvalidEmailException("Email already has been used!");
        }

        if (user.getPassword().chars().filter(Character::isUpperCase).count() < 2 || user.getPassword().length() < 6) {
            throw new PasswordRequirementsException("Password doesnt meet security requirements!");
        }

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
