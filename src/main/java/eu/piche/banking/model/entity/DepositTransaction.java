package eu.piche.banking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@DiscriminatorValue("DEPOSIT")
@NoArgsConstructor
public class DepositTransaction extends Transaction {

    public DepositTransaction(Long id, Instant timestamp, Long accountNumber, Long amount) {
        super(id, timestamp, accountNumber, amount);
    }

    @Override
    public DepositTransaction withId(Long id) {
        return new DepositTransaction(id, getTimestamp(), getAccountNumber(), getAmount());
    }

    @Override
    public DepositTransaction withTimestamp(Instant timestamp) {
        return new DepositTransaction(getId(), timestamp, getAccountNumber(), getAmount());
    }

    @Override
    public DepositTransaction withAccountNumber(Long accountNumber) {
        return new DepositTransaction(getId(), getTimestamp(), accountNumber, getAmount());
    }

    @Override
    public DepositTransaction withAmount(Long amount) {
        return new DepositTransaction(getId(), getTimestamp(), getAccountNumber(), amount);
    }
}
