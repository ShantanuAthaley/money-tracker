package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands.AddMembers;
import dev.shantanu.money.tracker.household.contract.HouseholdCreatedResult;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class HouseholdCommandHandler {
    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;

    public HouseholdCommandHandler(HouseholdRepository repository, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;
    }

    public Optional<HouseholdCreatedResult> handleAddMember(AddMembers command) {
        Optional<Household> householdById = repository.findById(command.householdId().id());
        return householdById.map(household -> {
            Set<Ids.PersonId> personIds = personCreationPort.addMembers(householdById.get().getHouseholdId(), command.personDraftSet());
            return new HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
        });
    }

    public HouseholdCreatedResult handleCreate(HouseholdCommands.Create createCommand) {
        Household householdWithNameOnly = Household.createWithName(createCommand.name());
        Household household = this.repository.save(householdWithNameOnly);
        Set<Ids.PersonId> personIds = personCreationPort.addMembers(household.getHouseholdId(), createCommand.members());
        return new HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
    }
}
