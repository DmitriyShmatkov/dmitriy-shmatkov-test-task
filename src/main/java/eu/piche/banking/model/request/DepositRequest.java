package eu.piche.banking.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class DepositRequest {
    @NotNull
    private Long accountNumber;

    @NotNull
    @Positive
    private Long amount;
}
