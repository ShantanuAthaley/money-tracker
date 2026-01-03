package dev.shantanu.money.tracker.account;

import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public interface AccountOwnershipPort {
    AccountOwnership createAccountOwnership(Ids.AccountId accountId, Set<AccountOwnership> accountOwners);
}
