package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.*;
import dev.shantanu.money.tracker.common.Ids;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
class AccountServiceImpl implements AccountService, AccountOwnershipPort {
    private final AccountCommandHandler accountCommandHandler;
    private final AccountQueryHandler accountQueryHandler;

    public AccountServiceImpl(AccountCommandHandler createAccountCommandHandler,
                              AccountQueryHandler accountQueryHandler) {
        this.accountCommandHandler = createAccountCommandHandler;
        this.accountQueryHandler = accountQueryHandler;
    }

    @Override
    public Long createAccount(CreateAccountCommand createAccountCommand) {
        return accountCommandHandler.createAccount(createAccountCommand);
    }

    @Override
    public Optional<AccountResult> getAccountResult(Long id) {
        AccountQuery.GetAccountResultByIdQuery query = new AccountQuery.GetAccountResultByIdQuery(id);
        return accountQueryHandler.getAccountResult(query);
    }


    @Override
    public AccountOwnership createAccountOwnership(Ids.AccountId accountId, Set<AccountOwnership> accountOwners) {
        CreateAccountOwnershipCommand  command = new CreateAccountOwnershipCommand(accountId.id(), accountOwners);
        accountCommandHandler.createOwnership(command);
        return null;
    }

    record CreateAccountOwnershipCommand(Long accountId, Set<AccountOwnership> accountOwners) {}
}
