package eu.piche.banking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@DiscriminatorValue("CREATE")
@NoArgsConstructor
public class CreateTransaction extends Transaction {

    public CreateTransaction(Long id, Instant timestamp, Long accountNumber, Long amount) {
        super(id, timestamp, accountNumber, amount);
    }

    @Override
    public CreateTransaction withId(Long id) {
        return new CreateTransaction(id, getTimestamp(), getAccountNumber(), getAmount());
    }

    @Override
    public CreateTransaction withTimestamp(Instant timestamp) {
        return new CreateTransaction(getId(), timestamp, getAccountNumber(), getAmount());
    }

    @Override
    public CreateTransaction withAccountNumber(Long accountNumber) {
        return new CreateTransaction(getId(), getTimestamp(), accountNumber, getAmount());
    }

    @Override
    public CreateTransaction withAmount(Long amount) {
        return new CreateTransaction(getId(), getTimestamp(), getAccountNumber(), amount);
    }
}
