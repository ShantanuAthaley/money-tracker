package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands;
import dev.shantanu.money.tracker.person.domain.command.PersonResult;

import java.util.Set;

public interface PersonCreationPort {
    Set<Ids.PersonId> addMembers(Ids.HouseholdId householdId, Set<HouseholdCommands.Create.PersonDraft> personDraft);

    Set<Ids.PersonId> getMemberIds(Ids.HouseholdId householdId);

    Set<PersonResult> getMembers(Ids.HouseholdId householdId);
}
