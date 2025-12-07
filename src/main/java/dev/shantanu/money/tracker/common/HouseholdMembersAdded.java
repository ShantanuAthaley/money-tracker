package dev.shantanu.money.tracker.common;

import java.util.Set;

public record HouseholdMembersAdded(Ids.HouseholdId householdId, Set<Ids.PersonId> personIdSet){}
