package eu.piche.banking.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@DiscriminatorValue("TRANSFER")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class TransferTransaction extends Transaction {
    private Long recipientAccountNumber;

    public TransferTransaction(Long id, Instant timestamp, Long accountNumber, Long amount, Long recipientAccountNumber) {
        super(id, timestamp, accountNumber, amount);
        this.recipientAccountNumber = recipientAccountNumber;
    }

    @Override
    public TransferTransaction withId(Long id) {
        return new TransferTransaction(id, getTimestamp(), getAccountNumber(), getAmount(), recipientAccountNumber);
    }

    @Override
    public TransferTransaction withTimestamp(Instant timestamp) {
        return new TransferTransaction(getId(), timestamp, getAccountNumber(), getAmount(), recipientAccountNumber);
    }

    @Override
    public TransferTransaction withAccountNumber(Long accountNumber) {
        return new TransferTransaction(getId(), getTimestamp(), accountNumber, getAmount(), recipientAccountNumber);
    }

    @Override
    public TransferTransaction withAmount(Long amount) {
        return new TransferTransaction(getId(), getTimestamp(), getAccountNumber(), amount, recipientAccountNumber);
    }

    public TransferTransaction withRecipientAccountNumber(Long recipientAccountNumber) {
        return new TransferTransaction(getId(), getTimestamp(), getAccountNumber(), getAmount(), recipientAccountNumber);
    }
}
