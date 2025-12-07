package dev.shantanu.money.tracker.household.domain.command;

import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public record HouseholdCreatedResult(Ids.HouseholdId householdId, String householdName, Set<Ids.PersonId> personIds) {
}
