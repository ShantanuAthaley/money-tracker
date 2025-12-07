package dev.shantanu.money.tracker.household.domain.command;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.domain.HouseholdRepository;
import dev.shantanu.money.tracker.household.domain.PersonCreationPort;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.AddMembers;
import dev.shantanu.money.tracker.household.domain.Household;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class HouseholdAddMembersHandler {

    private  final HouseholdRepository repository;
    private final ApplicationEventPublisher publisher;
    private final PersonCreationPort personCreationPort;

    public HouseholdAddMembersHandler(HouseholdRepository repository, ApplicationEventPublisher publisher, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.publisher = publisher;
        this.personCreationPort = personCreationPort;
    }

    public Optional<HouseholdCreatedResult> handle(AddMembers command) {
        Optional<Household> householdById = repository.findById(command.householdId().id());
        return householdById.map(household -> {
            Set<Ids.PersonId> personIds = personCreationPort.addMembers(householdById.get().getHouseholdId(), command.personDraftSet());
            return new HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
        });

    }


}
