package eu.piche.banking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@DiscriminatorValue("WITHDRAW")
@NoArgsConstructor
public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(Long id, Instant timestamp, Long accountNumber, Long amount) {
        super(id, timestamp, accountNumber, amount);
    }

    @Override
    public WithdrawTransaction withId(Long id) {
        return new WithdrawTransaction(id, getTimestamp(), getAccountNumber(), getAmount());
    }

    @Override
    public WithdrawTransaction withTimestamp(Instant timestamp) {
        return new WithdrawTransaction(getId(), timestamp, getAccountNumber(), getAmount());
    }

    @Override
    public WithdrawTransaction withAccountNumber(Long accountNumber) {
        return new WithdrawTransaction(getId(), getTimestamp(), accountNumber, getAmount());
    }

    @Override
    public WithdrawTransaction withAmount(Long amount) {
        return new WithdrawTransaction(getId(), getTimestamp(), getAccountNumber(), amount);
    }
}
