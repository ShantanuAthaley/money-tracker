package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.error.ApplicationException;
import dev.shantanu.money.tracker.error.ErrorCode;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands.AddMembers;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class HouseholdCommandHandler {
    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;
    private static final Logger LOGGER = LoggerFactory.getLogger(HouseholdCommandHandler .class);

    @SuppressWarnings("ClassEscapesDefinedScope")
    public HouseholdCommandHandler(HouseholdRepository repository,
                                   PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;
    }

    @Transactional
    public Optional<HouseholdCommands.HouseholdCreatedResult> handleAddMember(AddMembers command) {
        Optional<Household> householdById = repository.findById(command.householdId().id());
        return householdById.map(household -> {
            Set<Ids.PersonId> personIds = personCreationPort.addMembers(householdById.get().getHouseholdId(), command.personDraftSet());
            return buildHouseholdCreatedResult(household, personIds);
        });
    }

    @Transactional
    public HouseholdCommands.HouseholdCreatedResult handleCreate(HouseholdCommands.Create createCommand) {
        try {
            Household household = saveHouseholdWithName(createCommand.name());
            LOGGER.info("Created household with name {}, householdId = {} ", household.getName(), household.getHouseholdId());

            Set<Ids.PersonId> personIds = personCreationPort.addMembers(household.getHouseholdId(), createCommand.members());
            LOGGER.info("Successfully saved {} persons with household name  {}", personIds.size(), household.getHouseholdId());

            return buildHouseholdCreatedResult(household, personIds);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.DB_OPERATION, String.format("Failed to create household with name %s", createCommand.name()), e);
        }
    }

    private static HouseholdCommands.@NotNull HouseholdCreatedResult buildHouseholdCreatedResult(Household household, Set<Ids.PersonId> personIds) {
        return new HouseholdCommands.HouseholdCreatedResult(household.getHouseholdId(), household.getName(), personIds);
    }

    private @NotNull Household saveHouseholdWithName(final String name) {
        Household withName = Household.createWithName(name);
        return repository.save(withName);
    }
}
