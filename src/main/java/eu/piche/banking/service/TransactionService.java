package eu.piche.banking.service;

import eu.piche.banking.model.entity.Account;
import eu.piche.banking.model.entity.DepositTransaction;
import eu.piche.banking.model.entity.TransferTransaction;
import eu.piche.banking.model.entity.WithdrawTransaction;
import eu.piche.banking.model.request.DepositRequest;
import eu.piche.banking.model.request.TransferRequest;
import eu.piche.banking.model.request.WithdrawRequest;
import eu.piche.banking.repository.AccountRepository;
import eu.piche.banking.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(isolation = Isolation.SERIALIZABLE)
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void deposit(DepositRequest depositRequest) {
        Account account = accountRepository.findById(depositRequest.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account number not found"));
        account.setBalance(account.getBalance() + depositRequest.getAmount());
        accountRepository.save(account);
        transactionRepository.save(new DepositTransaction()
                .withTimestamp(Instant.now())
                .withAccountNumber(account.getAccountNumber())
                .withAmount(depositRequest.getAmount()));
    }

    public void withdraw(WithdrawRequest withdrawRequest) {
        Account account = accountRepository.findById(withdrawRequest.getAccountNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account number not found"));
        long newBalance = account.getBalance() - withdrawRequest.getAmount();
        if (newBalance < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough means on the balance");
        }
        account.setBalance(newBalance);
        accountRepository.save(account);
        transactionRepository.save(new WithdrawTransaction()
                .withTimestamp(Instant.now())
                .withAccountNumber(account.getAccountNumber())
                .withAmount(withdrawRequest.getAmount()));
    }

    public void transfer(TransferRequest transferRequest) {
        Account from = accountRepository.findById(transferRequest.getFrom())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Sender account number not found"));
        Account to = accountRepository.findById(transferRequest.getTo())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipient account number not found"));
        if (from.getBalance() < transferRequest.getAmount()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough means on the sender balance");
        }
        from.setBalance(from.getBalance() - transferRequest.getAmount());
        to.setBalance(to.getBalance() + transferRequest.getAmount());
        accountRepository.save(from);
        accountRepository.save(to);
        transactionRepository.save(new TransferTransaction()
                .withTimestamp(Instant.now())
                .withAccountNumber(from.getAccountNumber())
                .withAmount(transferRequest.getAmount())
                .withRecipientAccountNumber(to.getAccountNumber()));
    }
}
