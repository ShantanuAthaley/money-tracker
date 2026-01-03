package dev.shantanu.money.tracker.household.contract;

import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public record HouseholdCreatedResult(Ids.HouseholdId householdId, String householdName, Set<Ids.PersonId> personIds) {
}
