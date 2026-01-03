package dev.shantanu.money.tracker.person;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.Ids.PersonId;
import dev.shantanu.money.tracker.common.PersonDraft;
import dev.shantanu.money.tracker.common.PersonResult;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.person.domain.PersonCommandHandler;
import dev.shantanu.money.tracker.person.domain.PersonQueryHandler;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PersonService implements PersonCreationPort{
    private final PersonQueryHandler personQueryHandler;
    private final PersonCommandHandler personCommandHandler;

    public PersonService(PersonQueryHandler personQueryHandler, PersonCommandHandler personCommandHandler) {
        this.personQueryHandler = personQueryHandler;
        this.personCommandHandler = personCommandHandler;
    }


    @Override
    public Set<PersonId> addMembers(Ids.HouseholdId householdId, Set<PersonDraft> membersToCreate) {
        //Validate household does not contain the members already present
        Set<String> existingMemberEmails = getMembers(householdId)
                .stream()
                .map(PersonResult::email)
                .collect(Collectors.toSet());

        Predicate<PersonDraft> isAlreadyMember = draft -> existingMemberEmails.contains(draft.email());

        Set<PersonDraft> newMembers = membersToCreate.stream()
                .filter(isAlreadyMember.negate())
                .collect(Collectors.toSet());

        return personCommandHandler.createPersonForHousehold(householdId, newMembers);
    }

    @Override
    public Set<PersonId> getMemberIds(Ids.HouseholdId householdId) {
        Set<PersonResult> personsByHousehold = personQueryHandler.getPersonsByHousehold(householdId.id());
        return personsByHousehold
                .stream()
                .map(PersonResult::personId)
                .map(PersonId::new)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<PersonResult> getMembers(Ids.HouseholdId householdId) {
        return personQueryHandler.getPersonsByHousehold(householdId.id());
    }

}
