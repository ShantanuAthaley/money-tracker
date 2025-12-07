package dev.shantanu.money.tracker.person.domain.command;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.person.Person;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonResult(Ids.HouseholdId householdId,
                           Long personId,
                           String email,
                           String taxId,
                           LocalDate dateOfBirth,
                           LocalDateTime createdDateTime,
                           LocalDateTime updatedDateTime) {
    public static PersonResult from(Person person) {
        return new PersonResult(person.getHouseholdId(),
                person.id(),
                person.getEmail(),
                person.getTaxId(),
                person.getDateOfBirth(),
                person.getCreatedDateTime(),
                person.getUpdatedDateTime()
        );
    }
}
