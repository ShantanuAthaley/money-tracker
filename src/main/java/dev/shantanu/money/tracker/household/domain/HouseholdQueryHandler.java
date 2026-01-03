package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.household.contract.HouseholdQueries;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
class HouseholdQueryHandler {
    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;

    public HouseholdQueryHandler(HouseholdRepository repository, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;
    }

    public Set<Ids.PersonId> getMembers(HouseholdQueries.GetMembers getMemberQuery) {
        return repository.findById(getMemberQuery.householdId().id())
                .map(household -> personCreationPort.getMemberIds(household.getHouseholdId()))
                .orElse(Collections.emptySet());
    }
}
