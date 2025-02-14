package eu.piche.banking.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Entity
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;
    private Long balance;
}
