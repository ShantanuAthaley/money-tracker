package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.AccountResult;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class AccountQueryHandler {
    private final AccountRepository repository;
    public AccountQueryHandler(AccountRepository repository) {
        this.repository = repository;
    }

    Optional<AccountResult> getAccountResult(AccountQuery.GetAccountResultByIdQuery query) {
        Optional<Account> byId = repository.findById(query.id());
        return byId.map(Account::toAccountResult);
    }
}