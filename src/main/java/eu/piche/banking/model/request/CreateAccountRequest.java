package eu.piche.banking.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
public class CreateAccountRequest {

    @NotNull
    @PositiveOrZero
    private Long balance;
}
