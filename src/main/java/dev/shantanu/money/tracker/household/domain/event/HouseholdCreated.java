package dev.shantanu.money.tracker.household.domain.event;

import dev.shantanu.money.tracker.common.Ids.HouseholdId;

public record HouseholdCreated(HouseholdId householdId, String name){}
