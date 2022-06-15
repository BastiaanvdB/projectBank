package io.swagger.service;

import io.swagger.model.DTO.AccountDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.AccountNotFoundException;
import io.swagger.model.exception.InvalidIbanException;
import io.swagger.model.exception.InvalidPincodeException;
import io.swagger.model.exception.UserNotFoundException;
import io.swagger.repository.AccountRepository;
import io.swagger.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import static io.swagger.configuration.BankConstants.*;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserService userService;
    private final Random random = new Random();
    private final ModelMapper modelMapper;

    public AccountService() {
        this.modelMapper = new ModelMapper();
    }


    // ** Create an account + underlying sub methods
    public Account createAccount(AccountDTO body) throws UserNotFoundException {
        Account account = this.modelMapper.map(body, Account.class);
        User employee = userService.findByEmail(getUsernameFromBearer());
        User user = userService.getOne(body.getUserId());

        Account finalAccount = setAccountProperties(account, employee);
        accountRepository.save(finalAccount);
        userService.addAccountToUser(user, finalAccount);
        return finalAccount;
    }

    private Account setAccountProperties(Account account, User employee) {
        account.setIban(this.generateIban());
        account.setBalance(DEFAULT_ACCOUNT_BALANCE);
        account.setActivated(DEFAULT_ACCOUNT_ACTIVATION);
        account.setAbsoluteLimit(DEFAULT_ACCOUNT_ABSOLUTE_LIMIT);
        account.setPin(generatePin());
        account.setEmployeeId(employee.getId());
        return account;
    }

    private String generateIban() {
        String lastIban = this.getLastAccount().getIban();
        return generatePrefix(lastIban.substring(0, 9), lastIban.substring(9)) + generateIdentifier(lastIban.substring(9));
    }

    private String generateIdentifier(String identifier) {
        if (identifier.equals("999999999"))
            return "000000001";

        int number = Integer.parseInt(identifier) + 1;
        int amountOfDigits = String.valueOf(number).length();
        return "0".repeat(Math.max(0, 9 - amountOfDigits)) + number;
    }

    private String generatePrefix(String prefix, String identifier) {
        if (identifier.equals("999999999")) {
            int prefixNumber = Integer.parseInt(prefix.substring(2, 4)) + 1;
            if (String.valueOf(prefixNumber).length() == 1) {
                prefix = "NL0" + prefixNumber + IBAN_BANK_PREFIX;
            } else {
                prefix = "NL" + prefixNumber + IBAN_BANK_PREFIX;
            }
        }
        return prefix;
    }

    private String generatePin() {
        return passwordEncoder.encode(String.format("%04d", random.nextInt(10000)));
    }




    // ** Get one account for iban + underlying sub methods
    public Account getOneByIban(String iban) throws AccountNotFoundException, InvalidIbanException {
        isValidIban(iban);
        Account account = accountRepository.findAccountByIban(iban);
        if (account == null) {
            throw new AccountNotFoundException("User with this iban not found");
        }
        return accountRepository.findAccountByIban(iban);
    }

    private void isValidIban(String iban) throws InvalidIbanException {
        isIbanFromBank(iban);
        isIbanLengthValid(iban);
        isIbanCountryValid(iban);
        isIbanPrefixValid(iban);
        isIbanIdentifiersValid(iban);
    }

    private void isIbanFromBank(String iban) throws InvalidIbanException {
        if (iban.equals(IBAN_BANK) && !EMAIL_BANK.equals(this.getUsernameFromBearer()))
            throw new InvalidIbanException("No access to this account.");
    }
    private void isIbanLengthValid(String iban) throws InvalidIbanException {
        if (iban.length() != 18)
            throw new InvalidIbanException("Iban must be 18 characters long.");
    }
    private void isIbanCountryValid(String iban) throws InvalidIbanException {
        if (!iban.startsWith(IBAN_COUNTRY_PREFIX))
            throw new InvalidIbanException("Wrong country prefix, only NL is accepted.");
    }
    private void isIbanPrefixValid(String iban) throws InvalidIbanException {
        if (!iban.substring(2, 4).matches(REGEX_NUMBERS_ONLY) || !iban.substring(10, 18).matches(REGEX_NUMBERS_ONLY))
            throw new InvalidIbanException("Iban identifiers can only contain numbers.");
    }
    private void isIbanIdentifiersValid(String iban) throws InvalidIbanException {
        if (!iban.startsWith(IBAN_BANK_PREFIX, 4))
            throw new InvalidIbanException("Wrong bank prefix, only INHO0 is accepted.");
    }




    // ** Get all accounts + underlying sub methods
    public List<Account> getAll(int offset, int limit) {
        return accountRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }
    public List<Account> getAllByFirstname(String firstname, int offset, int limit) {
        return accountRepository.findAllByFirstname(PageRequest.of(offset, limit), firstname);
    }
    public List<Account> getAllByLastname(String lastname, int offset, int limit) {
        return accountRepository.findAllByLastname(PageRequest.of(offset, limit), lastname);
    }
    public List<Account> getAllByFirstAndLastname(String firstname, String lastname, int offset, int limit) {
        return accountRepository.findAllByFirstAndLastname(PageRequest.of(offset, limit), firstname, lastname);
    }




    // ** Get all accounts for a user
    public List<Account> getAllByUserId(int userId) {
        List<Account> accounts = accountRepository.findAllByUserid(userId);

        accounts.removeIf(account -> !account.getActivated());
        return accounts;
    }




    // ** Set new limit for account
    public void updateLimit(String iban, BigDecimal limit) throws InvalidIbanException, AccountNotFoundException {
        Account account = getOneByIban(iban);
        account.setAbsoluteLimit(limit);
        accountRepository.updateLimit(account.getAbsoluteLimit(), account.getIban());
    }




    // ** Set new pin for account + underlying sub methods
    public void updatePin(Account account, String oldPin, String newPin) throws InvalidPincodeException {
        isOldPinValid(account, oldPin);
        isNewPinValid(newPin);
        account.setPin(passwordEncoder.encode(newPin));
        accountRepository.updatePin(account.getPin(), account.getIban());
    }
    private void isOldPinValid(Account account, String oldPin) throws InvalidPincodeException {
        if (!passwordEncoder.matches(oldPin, account.getPin()) && !jwtTokenProvider.getAuthentication(getValidatedToken()).getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
            throw new InvalidPincodeException("Wrong pincode");
        }
    }
    private void isNewPinValid(String newPin) throws InvalidPincodeException {
        if (!newPin.matches(REGEX_NUMBERS_ONLY) || newPin.length() != 4) {
            throw new InvalidPincodeException("Pincode must only contain 4 digits");
        }
    }




    // ** Set new status for account
    public void updateStatus(String iban, Boolean isActivated) throws InvalidIbanException, AccountNotFoundException {
        Account account = getOneByIban(iban);
        account.setActivated(isActivated);
        accountRepository.updateStatus(account.getActivated(), account.getIban());
    }




    // ** Authenticate account
    public Boolean authenticateAccount(String iban, String pin) throws InvalidIbanException, AccountNotFoundException {
        Account account = getOneByIban(iban);
        return passwordEncoder.matches(pin, account.getPin());
    }




    // !!((Van Cees??))!!
    public void updateBalance(Account account) {
        accountRepository.updateLimit(account.getAbsoluteLimit(), account.getIban());
    }



    // ** HELPER METHODS
    private String getUsernameFromBearer() {
        return jwtTokenProvider.getUsername(getValidatedToken());
    }

    private String getValidatedToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = jwtTokenProvider.resolveToken(request);

        if (token == null || !jwtTokenProvider.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Token invalid or expired");
        }
        return token;
    }

    public Account getLastAccount() {
        return accountRepository.findLastAccountEntry();
    }

    public void createFromSeeder(Account account) {
        account.setPin(passwordEncoder.encode(account.getPin()));
        accountRepository.save(account);
    }
}