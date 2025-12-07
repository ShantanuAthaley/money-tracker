package dev.shantanu.money.tracker.household.domain.query;

import dev.shantanu.money.tracker.common.Ids;

public interface HouseholdQueries {
    record GetMembers(Ids.HouseholdId householdId){}
}
