package dev.shantanu.money.tracker.person.domain;

import dev.shantanu.money.tracker.account.AccountOwnership;
import dev.shantanu.money.tracker.account.AccountType;
import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public interface PersonCommand {
    record CreatePersonWithAccount(Ids.HouseholdId householdId, Ids.PersonId personId, Set<AccountOwnership> accountOwners) {
    }
    record CreateAccountOwnership(Ids.PersonId personId, Ids.AccountId accountId, AccountType accountType, Double ownershipPercentage) {
    }
}
