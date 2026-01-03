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

    private final HouseholdCreateHandler createHandler;
    private final HouseholdAddMembersHandler addMembersHandler;
    private final HouseholdQueryHandler queryHandler;

    public HouseholdServiceImpl(HouseholdCreateHandler createHandler,
                         HouseholdAddMembersHandler addMembersHandler,
                         HouseholdQueryHandler queryHandler) {
        this.createHandler = createHandler;
        this.addMembersHandler = addMembersHandler;
        this.queryHandler = queryHandler;
    }

    @Override
    public HouseholdCreatedResult createHouseHold(Create createCommand) {
        return createHandler.handle(createCommand);
    }

    @Override
    public Optional<HouseholdCreatedResult> addMembers(AddMembers addMembersCommand) {
        return addMembersHandler.handle(addMembersCommand);
    }

    @Override
    public Set<Ids.PersonId> getAllPersonIds(HouseholdQueries.GetMembers getMembersQuery) {
        return queryHandler.getMembers(getMembersQuery);
    }


}
