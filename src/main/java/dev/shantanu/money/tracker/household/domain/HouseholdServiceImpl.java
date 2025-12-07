package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.HouseholdService;
import dev.shantanu.money.tracker.household.domain.command.HouseholdAddMembersHandler;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.AddMembers;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.Create;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCreateHandler;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCreatedResult;
import dev.shantanu.money.tracker.household.domain.query.HouseholdQueries;
import dev.shantanu.money.tracker.household.domain.query.HouseholdQueryHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
class HouseholdServiceImpl implements HouseholdService {

    private final HouseholdCreateHandler createHandler;
    private final HouseholdAddMembersHandler addMembersHandler;
    private final HouseholdQueryHandler queryHandler;

    HouseholdServiceImpl(HouseholdCreateHandler createHandler,
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
