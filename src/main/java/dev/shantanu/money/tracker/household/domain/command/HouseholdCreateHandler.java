package dev.shantanu.money.tracker.household.domain.command;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.domain.Household;
import dev.shantanu.money.tracker.household.domain.HouseholdRepository;
import dev.shantanu.money.tracker.household.domain.PersonCreationPort;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.Create;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class HouseholdCreateHandler {

    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;

    HouseholdCreateHandler(HouseholdRepository repository, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;
    }

    public HouseholdCreatedResult handle(Create createCommand) {
        Household householdWithNameOnly = Household.createWithName(createCommand.name());
        Household household = this.repository.save(householdWithNameOnly);
        Set<Ids.PersonId> personIds = personCreationPort.addMembers(household.getHouseholdId(), createCommand.members());
        return new HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
    }
}
