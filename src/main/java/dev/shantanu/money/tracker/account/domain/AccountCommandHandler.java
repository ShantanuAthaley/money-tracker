package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.AccountOwnership;
import dev.shantanu.money.tracker.account.CreateAccountCommand;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Component
class AccountCommandHandler {
    AccountRepository repository;

    public AccountCommandHandler(AccountRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Long createAccount(CreateAccountCommand createAccountCommand) {
        Account account = Account.fromCreateCommand(createAccountCommand);
        Account save = repository.save(account);
        return save.getAccountId();
    }

    @Transactional
    public void createOwnership(AccountServiceImpl.CreateAccountOwnershipCommand command) {
        Set<AccountOwnership> accountOwnerships = command.accountOwners();
        Objects.requireNonNull(accountOwnerships, "accountOwnerships must not be null");

        repository.findById(command.accountId())
                .ifPresent(account -> {
                    Set<AccountOwnership> accountOwners = account.getAccountOwners();
                    if (accountOwners != null && !accountOwners.isEmpty()) {
                        accountOwners.addAll(accountOwnerships);
                    } else {
                        account.setAccountOwners(accountOwnerships);
                    }
                });
    }
}
