package dev.shantanu.money.tracker.person;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.domain.PersonCreationPort;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.Create.PersonDraft;
import dev.shantanu.money.tracker.person.domain.PersonRepository;
import dev.shantanu.money.tracker.person.domain.command.PersonResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PersonService implements PersonCreationPort {
    PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public Set<Ids.PersonId> addMembers(Ids.HouseholdId householdId, Set<PersonDraft> membersToCreate) {
        Set<Person> collect = Optional.of(membersToCreate)
                .orElse(Collections.emptySet())
                .stream()
                .map(member -> from(householdId.id(), member))
                .collect(Collectors.toSet());

        List<Person> people = personRepository.saveAll(collect);

        return Optional.of(people)
                .orElse(Collections.emptyList())
                .stream()
                .map(Person::id)
                .map(Ids.PersonId::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Ids.PersonId> getMemberIds(Ids.HouseholdId householdId) {
        Set<Long> personIds = personRepository.getAllPersonIdByHouseholdId(householdId.id());
        return Optional.of(personIds)
                .orElse(Collections.emptySet())
                .stream()
                .map(Ids.PersonId::new)
                .collect(Collectors.toSet());

    }

    @Override
    public Set<PersonResult> getMembers(Ids.HouseholdId householdId) {
        return personRepository.getAllByHouseholdId(householdId.id())
                .stream()
                .map(PersonResult::from)
                .collect(Collectors.toSet());
    }

    static Person from(Long householdId, PersonDraft memberToPersist) {
        return new Person(null,
                householdId,
                memberToPersist.name(),
                memberToPersist.taxId(),
                memberToPersist.email(),
                memberToPersist.dateOfBirth(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

}
