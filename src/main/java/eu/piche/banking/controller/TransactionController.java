package eu.piche.banking.controller;

import eu.piche.banking.model.request.DepositRequest;
import eu.piche.banking.model.request.TransferRequest;
import eu.piche.banking.model.request.WithdrawRequest;
import eu.piche.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<Void> deposit(@RequestBody @Validated DepositRequest depositRequest) {
        transactionService.deposit(depositRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(@RequestBody @Validated WithdrawRequest withdrawRequest) {
        transactionService.withdraw(withdrawRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody @Validated TransferRequest transferRequest) {
        transactionService.transfer(transferRequest);
        return ResponseEntity.ok().build();
    }
}
