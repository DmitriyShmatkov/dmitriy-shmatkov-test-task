package eu.piche.banking.service;

import eu.piche.banking.model.entity.*;
import eu.piche.banking.model.request.DepositRequest;
import eu.piche.banking.model.request.TransferRequest;
import eu.piche.banking.model.request.WithdrawRequest;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;
    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void deposit() {
        var request = new DepositRequest(1L, 50L);
        var account = new Account(1L, 100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        transactionService.deposit(request);

        verify(accountRepository).save(accountCaptor.capture());
        Account updatedAccount = accountCaptor.getValue();
        assertThat(updatedAccount.getAccountNumber()).isEqualTo(1L);
        assertThat(updatedAccount.getBalance()).isEqualTo(150L);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction).isInstanceOf(DepositTransaction.class);
        assertThat(savedTransaction.getTimestamp()).isNotNull();
        assertThat(savedTransaction.getAccountNumber()).isEqualTo(1L);
        assertThat(savedTransaction.getAmount()).isEqualTo(50L);
    }

    @Test
    void depositAccountNotFound() {
        var request = new DepositRequest(1L, 50L);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.deposit(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Account number not found");
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void withdraw() {
        var request = new WithdrawRequest(1L, 50L);
        var account = new Account(1L, 100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        transactionService.withdraw(request);

        verify(accountRepository).save(accountCaptor.capture());
        var updatedAccount = accountCaptor.getValue();
        assertThat(updatedAccount.getAccountNumber()).isEqualTo(1L);
        assertThat(updatedAccount.getBalance()).isEqualTo(50L);

        verify(transactionRepository).save(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();
        assertThat(savedTransaction).isInstanceOf(WithdrawTransaction.class);
        assertThat(savedTransaction.getTimestamp()).isNotNull();
        assertThat(savedTransaction.getAccountNumber()).isEqualTo(1L);
        assertThat(savedTransaction.getAmount()).isEqualTo(50L);
    }

    @Test
    void withdrawAccountNotFound() {
        var request = new WithdrawRequest(1L, 50L);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.withdraw(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Account number not found");
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void withdrawAccountNotEnoughBalance() {
        var request = new WithdrawRequest(1L, 150L);
        var account = new Account(1L, 100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> transactionService.withdraw(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("Not enough means on the balance");
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transfer() {
        var request = new TransferRequest(1L, 2L, 50L);
        Account from = new Account(1L, 100L);
        Account to = new Account(2L, 200L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));

        transactionService.transfer(request);

        verify(accountRepository, times(2)).save(accountCaptor.capture());
        Account updatedFrom = accountCaptor.getAllValues().get(0);
        Account updatedTo = accountCaptor.getAllValues().get(1);
        assertThat(updatedFrom.getAccountNumber()).isEqualTo(1L);
        assertThat(updatedFrom.getBalance()).isEqualTo(50L);
        assertThat(updatedTo.getAccountNumber()).isEqualTo(2L);
        assertThat(updatedTo.getBalance()).isEqualTo(250L);

        verify(transactionRepository).save(transactionCaptor.capture());
        assertThat(transactionCaptor.getValue()).isInstanceOf(TransferTransaction.class);
        TransferTransaction savedTransaction = (TransferTransaction) transactionCaptor.getValue();
        assertThat(savedTransaction.getTimestamp()).isNotNull();
        assertThat(savedTransaction.getAccountNumber()).isEqualTo(1L);
        assertThat(savedTransaction.getAmount()).isEqualTo(50L);
        assertThat(savedTransaction.getRecipientAccountNumber()).isEqualTo(2L);
    }

    @Test
    void transferFromNotFound() {
        var request = new TransferRequest(1L, 2L, 50L);
        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.transfer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Sender account number not found");
        verify(accountRepository).findById(1L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void transferToNotFound() {
        var request = new TransferRequest(1L, 2L, 50L);
        var from = new Account(1L, 100L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> transactionService.transfer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("404")
                .hasMessageContaining("Recipient account number not found");
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }


    @Test
    void transferNotEnoughBalance() {
        var request = new TransferRequest(1L, 2L, 150L);
        Account from = new Account(1L, 100L);
        Account to = new Account(2L, 200L);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(to));


        assertThatThrownBy(() -> transactionService.transfer(request))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400")
                .hasMessageContaining("Not enough means on the sender balance");
        verify(accountRepository).findById(1L);
        verify(accountRepository).findById(2L);
        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}