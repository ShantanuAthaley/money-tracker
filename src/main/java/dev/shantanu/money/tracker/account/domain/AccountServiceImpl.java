package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.AccountOwnershipPort;
import dev.shantanu.money.tracker.account.AccountResult;
import dev.shantanu.money.tracker.account.AccountService;
import dev.shantanu.money.tracker.account.CreateAccountCommand;
import dev.shantanu.money.tracker.common.Ids;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void createAccountOwnership(Ids.AccountId accountId, Map<Long, Double> accountOwners) {
        Set<AccountOwnership> accountOwnershipSet = Optional.ofNullable(accountOwners).orElse(new HashMap<>())
                .entrySet().stream()
                .map(ownerEntry -> new AccountOwnership(ownerEntry.getKey(), ownerEntry.getValue()))
                .collect(Collectors.toSet());
        CreateAccountOwnershipCommand  command = new CreateAccountOwnershipCommand(accountId.id(), accountOwnershipSet);
        accountCommandHandler.createOwnership(command);
    }

    record CreateAccountOwnershipCommand(Long accountId, Set<AccountOwnership> accountOwners) {}
}
