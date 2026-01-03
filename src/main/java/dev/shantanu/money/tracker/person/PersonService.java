package dev.shantanu.money.tracker.person;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.Ids.PersonId;
import dev.shantanu.money.tracker.common.PersonDraft;
import dev.shantanu.money.tracker.common.PersonResult;
import dev.shantanu.money.tracker.household.PersonCreationPort;
import dev.shantanu.money.tracker.person.domain.PersonCommandHandler;
import dev.shantanu.money.tracker.person.domain.PersonQueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class PersonService implements PersonCreationPort{
    private final PersonQueryHandler personQueryHandler;
    private final PersonCommandHandler personCommandHandler;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);

    public PersonService(PersonQueryHandler personQueryHandler, PersonCommandHandler personCommandHandler) {
        this.personQueryHandler = personQueryHandler;
        this.personCommandHandler = personCommandHandler;
    }


    @Override
    @Transactional
    public Set<PersonId> addMembers(Ids.HouseholdId householdId, Set<PersonDraft> membersToCreate) {
        //Validate household does not contain the members already present
        Set<PersonResult> existingMembers = getMembers(householdId);
        Set<String> existingMemberTaxId = existingMembers.stream()
                .map(PersonResult::taxId)
                .collect(Collectors.toSet());
        Set<String> existingMemberEmails = existingMembers
                .stream()
                .map(PersonResult::email)
                .collect(Collectors.toSet());

        Predicate<PersonDraft> isAlreadyMemberByEmail = draft -> existingMemberEmails.contains(draft.email());
        Predicate<PersonDraft> isAlreadyMemberByTaxId = draft -> existingMemberTaxId.contains(draft.taxId());

        Set<PersonDraft> newMembers = membersToCreate.stream()
                .filter(isAlreadyMemberByEmail.negate())
                .filter(isAlreadyMemberByTaxId.negate())
                .collect(Collectors.toSet());

        if(newMembers.isEmpty()) {
            LOGGER.warn("Member are already for household ID = {}", householdId);
        }
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
