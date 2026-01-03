package dev.shantanu.money.tracker.household;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonDraft;
import dev.shantanu.money.tracker.common.PersonResult;

import java.util.Set;

public interface PersonCreationPort {
    Set<Ids.PersonId> addMembers(Ids.HouseholdId householdId, Set<PersonDraft> personDraft);

    Set<Ids.PersonId> getMemberIds(Ids.HouseholdId householdId);

    Set<PersonResult> getMembers(Ids.HouseholdId householdId);
}
