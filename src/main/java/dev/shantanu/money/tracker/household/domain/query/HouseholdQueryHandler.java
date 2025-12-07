package dev.shantanu.money.tracker.household.domain.query;

import dev.shantanu.money.tracker.common.Ids.PersonId;
import dev.shantanu.money.tracker.household.domain.HouseholdRepository;
import dev.shantanu.money.tracker.household.domain.PersonCreationPort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class HouseholdQueryHandler {
    private final HouseholdRepository repository;
    private final PersonCreationPort personCreationPort;


    public HouseholdQueryHandler(HouseholdRepository repository, PersonCreationPort personCreationPort) {
        this.repository = repository;
        this.personCreationPort = personCreationPort;


    }

    public Set<PersonId> getMembers(HouseholdQueries.GetMembers getMemberQuery) {
        return repository.findById(getMemberQuery.householdId().id())
                .map(household -> personCreationPort.getMemberIds(household.getHouseholdId()))
                .orElse(Collections.emptySet());
    }

}
