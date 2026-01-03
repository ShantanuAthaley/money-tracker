package dev.shantanu.money.tracker.household.contract;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonDraft;

import java.util.Set;

public interface HouseholdCommands {
    record Create(String name, Set<PersonDraft> members) {
    }
    record AddMembers(Ids.HouseholdId householdId, Set<PersonDraft> personDraftSet) {
    }
    record HouseholdCreatedResult(Ids.HouseholdId householdId, String householdName, Set<Ids.PersonId> personIds) {
    }
}

