package dev.shantanu.money.tracker.person.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonQueryHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonQueryHandler.class);

    private PersonRepository personRepository;

    public PersonQueryHandler(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Set<PersonResult> getPersonsByHousehold(Long householdId) {
        Objects.requireNonNull(householdId, "householdId cannot be null");

        Set<Person> allByHouseholdId = personRepository.getAllByHouseholdId(householdId);
        return Optional.ofNullable(allByHouseholdId)
                .orElse(Set.of())
                .stream()
                .map(Person::toPersonResult)
                .collect(Collectors.toSet());
    }

    public Optional<PersonResult> getPersonById(PersonQuery.GetPersonById getPersonById) {
        Objects.requireNonNull(getPersonById, "getPersonById must not be null");
        Ids.PersonId personId = getPersonById.personId();
        Objects.requireNonNull(personId, "personId must not be null");

        return personRepository.findById(personId.id())
                .map(Person::toPersonResult);
    }
}
