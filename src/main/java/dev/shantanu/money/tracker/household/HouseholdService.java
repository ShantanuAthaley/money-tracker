package dev.shantanu.money.tracker.household;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands.Create;
import dev.shantanu.money.tracker.household.contract.HouseholdCreatedResult;
import dev.shantanu.money.tracker.household.contract.HouseholdQueries;

import java.util.Optional;
import java.util.Set;

public interface HouseholdService {
    HouseholdCreatedResult createHouseHold(Create createCommand);

    Optional<HouseholdCreatedResult> addMembers(HouseholdCommands.AddMembers addMembersCommand);

    Set<Ids.PersonId> getAllPersonIds(HouseholdQueries.GetMembers getMembersQuery);
}
