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
public class TransferRequest {
    @NotNull
    private Long from;
    @NotNull
    private Long to;
    @NotNull
    @Positive
    private Long amount;
}
