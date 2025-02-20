package eu.piche.banking.service;

import eu.piche.banking.model.entity.Account;
import eu.piche.banking.model.entity.CreateTransaction;
import eu.piche.banking.model.request.CreateAccountRequest;
import eu.piche.banking.repository.AccountRepository;
import eu.piche.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.READ_COMMITTED)
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public Account getAccount(Long accountNumber) {
        return accountRepository.findById(accountNumber)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account number not found"));
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Account createAccount(CreateAccountRequest request) {
        Account account = accountRepository.save(new Account().withBalance(request.getBalance()));
        transactionRepository.save(new CreateTransaction()
                .withTimestamp(Instant.now())
                .withAccountNumber(account.getAccountNumber())
                .withAmount(account.getBalance()));
        return account;
    }
}
