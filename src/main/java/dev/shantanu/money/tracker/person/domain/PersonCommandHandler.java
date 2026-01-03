package dev.shantanu.money.tracker.person.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonDraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class PersonCommandHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonCommandHandler.class);
    PersonRepository personRepository;

    public PersonCommandHandler(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Set<Ids.PersonId> createPersonForHousehold(Ids.HouseholdId householdId, Set<PersonDraft> personDraftSet) {
        Objects.requireNonNull(householdId, "householdId must not be null");
        Objects.requireNonNull(personDraftSet, "personDraftSet must not be null");

        Set<Person> personToSave = personDraftSet.stream()
                .map(personDraft -> Person.buildFrom(householdId, personDraft))
                .collect(Collectors.toSet());
        List<Person> people = personRepository.saveAll(personToSave);
        return people.stream()
                .map(Person::id)
                .map(Ids.PersonId::new)
                .collect(Collectors.toSet());
    }

}
