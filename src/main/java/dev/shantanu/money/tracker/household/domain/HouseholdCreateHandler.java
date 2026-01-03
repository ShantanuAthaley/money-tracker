package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands;
import dev.shantanu.money.tracker.household.contract.HouseholdCreatedResult;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
class HouseholdCreateHandler {

    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;

    public HouseholdCreateHandler(HouseholdRepository repository, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;
    }

    public HouseholdCreatedResult handle(HouseholdCommands.Create createCommand) {
        Household householdWithNameOnly = Household.createWithName(createCommand.name());
        Household household = this.repository.save(householdWithNameOnly);
        Set<Ids.PersonId> personIds = personCreationPort.addMembers(household.getHouseholdId(), createCommand.members());
        return new HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
    }
}
