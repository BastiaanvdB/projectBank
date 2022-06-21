package io.swagger.service;

import io.swagger.model.DTO.DepositDTO;
import io.swagger.model.DTO.TransactionDTO;
import io.swagger.model.DTO.WithdrawDTO;
import io.swagger.model.ResponseDTO.SpendResponseDTO;
import io.swagger.model.entity.Account;
import io.swagger.model.entity.Transaction;
import io.swagger.model.entity.User;
import io.swagger.model.enumeration.AccountType;
import io.swagger.model.enumeration.Role;
import io.swagger.model.exception.*;
import io.swagger.repository.AccountRepository;
import io.swagger.repository.TransactionRepository;
import io.swagger.repository.TransactionRepositoryImpl;
import io.swagger.security.JwtTokenProvider;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static io.swagger.configuration.BankConstants.IBAN_BANK;

@Service
public class TransactionService {
    private final ModelMapper modelMapper;
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    TransactionRepositoryImpl transactionRepoImpl;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRepository accountRepository;

    public TransactionService() {
        this.modelMapper = new ModelMapper();
    }

    private List<Transaction> getAll(Integer offset, Integer limit) {
        return transactionRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    private List<Transaction> getAll(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit) {

        return transactionRepoImpl.findAllCustom(startDate, endDate, ibanFrom, ibanTo, balanceOperator, balance, offset, limit);
    }

    public List<Transaction> getAllFromAccount(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit, String request) throws InvalidIbanException, AccountNotFoundException, UnauthorizedException, UserNotFoundException {
        User user = getUserFromToken(request);
        if (user.getRoles().contains(Role.ROLE_USER)) {
            if (!isUserOwner(user, ibanFrom)) {
                throw new UnauthorizedException("You have no acces to this account");
            }
        }

        if (offset == null) {offset = 0;} if (limit == null){limit =50;}

        List<Transaction> all = this.getAll(startDate, endDate, ibanFrom, ibanTo, balanceOperator, balance, offset, limit);
        all.addAll(this.getAll(startDate, endDate, ibanTo, ibanFrom, balanceOperator, balance, offset, limit));
        // sort the complete list desc. so that the last transaction is first
        all.sort(Comparator.comparing(Transaction::getIat).reversed());

        return all;
    }

    public Transaction createTransaction(TransactionDTO body, String request) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException, InvalidRoleException, InsufficientFundsException, ExcceedsLimitExeption, InvalidPincodeException {
        Transaction transaction = this.modelMapper.map(body, Transaction.class);

        // get accounts for checks
        Account accFrom = accountService.getOneByIban(transaction.getIbanFrom());
        Account accTo = accountService.getOneByIban(transaction.getIbanTo());
        User user = getUserFromToken(request);

        isPinCorrect(body.getPin(), accFrom);

        // employee part
        if (user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            isBankAccount(accFrom.getIban());
            return employeeTransaction(transaction, user);
        } else if (user == accFrom.getUser()) {
            if (accFrom.getType() == AccountType.SAVINGS || accTo.getType() == AccountType.SAVINGS) {
                if (accFrom.getUser() == accTo.getUser() || accTo.getUser() == accFrom.getUser()) {
                    savingsTransaction(transaction, user);
                }else {
                    throw new UnauthorizedException("This account does not belong to U");
                }
            } else {
                return normalTransaction(transaction, accFrom, user);
            }

        } else {
            throw new InvalidRoleException("You have no access to this account");
        }
        return transaction;
    }

    public Transaction deposit(DepositDTO body, String IBAN, String request) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {

        Transaction deposit = this.modelMapper.map(body, Transaction.class);

        // set the iban of the bank on the right position
        deposit.setIbanTo(IBAN);
        deposit.setIbanFrom(IBAN_BANK);

        User user = getUserFromToken(request);

        return this.doTransaction(deposit, user);
    }

    public Transaction withdraw(String IBAN, WithdrawDTO body, String request) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {

        Transaction withdraw = this.modelMapper.map(body, Transaction.class);
        // set the iban of the bank on the right position
        withdraw.setIbanFrom(IBAN);
        withdraw.setIbanTo(IBAN_BANK);

        User user = getUserFromToken(request);

        checkBalanceForTransaction(IBAN, withdraw.getAmount());

        return doTransaction(withdraw, user);
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }


    public List<Transaction> getAll(LocalDate startDate, LocalDate endDate, String ibanFrom, String ibanTo, String balanceOperator, String balance, Integer offset, Integer limit, String request) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {
        // Check if offset and limit is not empty otherwise give default readings
        if (offset == null) {
            offset = 0;
        }
        if (limit == null) {
            limit = 50;
        }

        User user = getUserFromToken(request);
        if (user.getRoles().contains(Role.ROLE_USER)) {
            if (!isUserOwner(user, ibanFrom)) {
                throw new UnauthorizedException("You have no acces to this account");
            }
        }

        // if there's no query just get them all with limit and offset
        if ((ibanFrom != null) || (ibanTo != null) || (balance != null) || (startDate != null) || (endDate != null)) {
            // get all the transactions with query
            return this.getAll(startDate, endDate, ibanFrom, ibanTo, balanceOperator, balance, offset, limit);
        } else {
            return this.getAll(offset, limit);
        }

    }



    // Private Functions
    public SpendResponseDTO getAllFromTodaySUM(String iban, String request) throws UserNotFoundException, UnauthorizedException, InvalidIbanException, AccountNotFoundException {
        User user = getUserFromToken(request);
        User account = getUserFromIban(iban);
        if (account.getId() != user.getId() && !user.getRoles().contains(Role.ROLE_EMPLOYEE)) {
            throw new UnauthorizedException("Not allowed to get spending of this account!");
        }
        BigDecimal var = transactionRepository.getAllFromTodaySUM(iban);
        if (var == null) {
            var = BigDecimal.ZERO;
        }
        return new SpendResponseDTO(var);
    }

    private User getUserFromIban(String iban) throws InvalidIbanException, AccountNotFoundException {
        return this.accountService.getOneByIban(iban).getUser();
    }

    private void isPinCorrect(String pin, Account accFrom) throws InvalidPincodeException {
        if (!passwordEncoder.matches(pin, accFrom.getPin())) {
            throw new InvalidPincodeException("Incorrect Pin");

        }
    }

    private User getUserFromToken(String token) throws UnauthorizedException, UserNotFoundException {
        if (token == null || !tokenProvider.validateToken(token)) {
            throw new UnauthorizedException("Token invalid or expired");
        }
        String username = tokenProvider.getUsername(token);
        User user = userService.findByEmail(username);
        if (user == null) {
            throw new UserNotFoundException("No user found with this token");
        }
        return user;
    }

    private void isBankAccount(String iban) throws UnauthorizedException {
        if (Objects.equals(iban, IBAN_BANK)) {
            throw new UnauthorizedException("No access to this account");
        }
    }

    private boolean checkBalanceForTransaction(String iban, BigDecimal amount) throws AccountNotFoundException, InvalidIbanException {
        Account acc = accountService.getOneByIban(iban);
        return (acc.getBalance().subtract(amount)).compareTo(acc.getAbsoluteLimit()) >= 0;
    }

    private Transaction doTransaction(Transaction transaction, User user) throws AccountNotFoundException, InvalidIbanException {
        transaction.setIssuedBy(user.getId());
        Transaction model = this.saveTransaction(transaction);

        Account from = accountRepository.findAccountByIban(transaction.getIbanFrom());
        Account to = accountRepository.findAccountByIban(transaction.getIbanTo());
        // When an account is null, no account was found with specified iban
        if (from == null || to == null) {
            throw new AccountNotFoundException("We have no account with this iban");
        }

        from.setBalance(from.getBalance().subtract(transaction.getAmount()));
        accountService.updateBalance(from);

        to.setBalance(to.getBalance().add(transaction.getAmount()));
        accountService.updateBalance(to);

        // Return the trans
        return model;
    }

    private Transaction employeeTransaction(Transaction transaction, User user) throws UnauthorizedException, InvalidIbanException, AccountNotFoundException, InsufficientFundsException {
        isBankAccount(transaction.getIbanFrom());
        if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
            throw new InsufficientFundsException("Not enough money on this account");
        } else {
            // Do Transaction and return response
            return this.doTransaction(transaction, user);
        }
    }

    private Transaction savingsTransaction(Transaction transaction, User user) throws InsufficientFundsException, InvalidIbanException, AccountNotFoundException {
        if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
            throw new InsufficientFundsException("Not enough money on this account");
        } else {
            // Do Transaction and return response
            return this.doTransaction(transaction, user);
        }
    }

    private Transaction normalTransaction(Transaction transaction, Account accFrom, User user) throws InvalidIbanException, AccountNotFoundException, InsufficientFundsException, ExcceedsLimitExeption {
        if (!checkBalanceForTransaction(transaction.getIbanFrom(), transaction.getAmount())) {
            throw new InsufficientFundsException("You have not enough money on this account");
        } else {
            //Check for day limit is not getting exceeded
            if (accFrom.getUser().getDayLimit().compareTo(transaction.getAmount().add(this.transactionRepository.getAllFromTodaySUM(accFrom.getIban()))) > 0) {
                // Check if the transaction is not exceeding the transaction limit
                if (accFrom.getUser().getTransactionLimit().compareTo(transaction.getAmount()) > 0) {
                    return this.doTransaction(transaction, user);
                } else {
                    throw new ExcceedsLimitExeption("This transaction exceeds the transaction limit");
                }
            } else {
                throw new ExcceedsLimitExeption("With this transaction day limit will be exceeded");
            }
        }
    }

    private boolean isUserOwner(User user, String iban) throws AccountNotFoundException, InvalidIbanException {
        Account acc = accountService.getOneByIban(iban);
        return acc.getUser() == user;
    }

}
