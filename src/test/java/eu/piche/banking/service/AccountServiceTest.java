package eu.piche.banking.service;

import eu.piche.banking.model.entity.Account;
import eu.piche.banking.model.entity.CreateTransaction;
import eu.piche.banking.model.entity.Transaction;
import eu.piche.banking.model.request.CreateAccountRequest;
import eu.piche.banking.repository.AccountRepository;
import eu.piche.banking.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void getAccount() {
        var account = new Account(1L, 100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Account result = accountService.getAccount(1L);

        verify(accountRepository).findById(1L);
        assertThat(result).isEqualTo(account);
    }

    @Test
    void getAccountNotFound() {
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.getAccount(1L))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Account number not found");
        verify(accountRepository).findById(1L);
    }

    @Test
    void getAllAccounts() {
        var account = new Account(1L, 100L);
        when(accountRepository.findAll()).thenReturn(List.of(account));

        List<Account> result = accountService.getAllAccounts();

        verify(accountRepository).findAll();
        assertThat(result).isEqualTo(List.of(account));
    }

    @Test
    void createAccount() {
        var request = new CreateAccountRequest(100L);
        var newAccount = new Account().withBalance(100L);
        var savedAccount = new Account(1L, 100L);
        when(accountRepository.save(newAccount)).thenReturn(savedAccount);

        var result = accountService.createAccount(request);

        verify(accountRepository).save(accountCaptor.capture());
        assertThat(accountCaptor.getValue()).isEqualTo(newAccount);
        assertThat(result).isEqualTo(savedAccount);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction).isInstanceOf(CreateTransaction.class);
        assertThat(savedTransaction.getTimestamp()).isNotNull();
        assertThat(savedTransaction.getAccountNumber()).isEqualTo(1L);
        assertThat(savedTransaction.getAmount()).isEqualTo(100L);
    }
}