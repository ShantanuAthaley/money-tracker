package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.HouseholdService;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands.AddMembers;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands.Create;
import dev.shantanu.money.tracker.household.contract.HouseholdCreatedResult;
import dev.shantanu.money.tracker.household.contract.HouseholdQueries;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
class HouseholdServiceImpl implements HouseholdService {

    private final HouseholdQueryHandler queryHandler;
    private final HouseholdCommandHandler commandHandler;

    public HouseholdServiceImpl(
            HouseholdQueryHandler queryHandler, HouseholdCommandHandler commandHandler) {
        this.queryHandler = queryHandler;
        this.commandHandler = commandHandler;
    }

    @Override
    public HouseholdCreatedResult createHouseHold(Create createCommand) {
        return commandHandler.handleCreate(createCommand);
    }

    @Override
    public Optional<HouseholdCreatedResult> addMembers(AddMembers addMembersCommand) {
        return commandHandler.handleAddMember(addMembersCommand);
    }

    @Override
    public Set<Ids.PersonId> getAllPersonIds(HouseholdQueries.GetMembers getMembersQuery) {
        return queryHandler.getMembers(getMembersQuery);
    }

}
