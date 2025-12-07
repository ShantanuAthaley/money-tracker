package dev.shantanu.money.tracker.person.domain.command;

import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public interface PersonCommand {
    record PersonCreated(Ids.HouseholdId householdId, Ids.PersonId personId,
                         Set<Ids.AccountId> accountIdSet) {
    }
}
