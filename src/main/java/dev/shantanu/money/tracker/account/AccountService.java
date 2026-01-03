package dev.shantanu.money.tracker.account;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AccountService {
    Long createAccount(CreateAccountCommand createAccountCommand);
    Optional<AccountResult> getAccountResult(Long id);
}
