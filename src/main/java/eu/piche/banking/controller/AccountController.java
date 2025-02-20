package eu.piche.banking.controller;

import eu.piche.banking.model.entity.Account;
import eu.piche.banking.model.request.CreateAccountRequest;
import eu.piche.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/account")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@RequestBody @Validated CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    @GetMapping("/account/{accountNumber}")
    public Account getAccount(@PathVariable Long accountNumber) {
        return accountService.getAccount(accountNumber);
    }

    @GetMapping("/accounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
}
