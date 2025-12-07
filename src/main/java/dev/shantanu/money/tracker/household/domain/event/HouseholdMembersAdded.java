package dev.shantanu.money.tracker.household.domain.event;

import dev.shantanu.money.tracker.common.Ids;

import java.util.Set;

public record HouseholdMembersAdded(String name, Set<Ids.PersonId> members) {
}
