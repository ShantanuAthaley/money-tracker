package dev.shantanu.money.tracker.household.domain.command;

import dev.shantanu.money.tracker.common.Ids;

import java.time.LocalDate;
import java.util.Set;

public interface HouseholdCommands {
    record Create(String name, Set<PersonDraft> members) {
        public record PersonDraft(
                String name,
                String taxId,
                String email,
                LocalDate dateOfBirth
        ) {
        }
    }

    record AddMembers(Ids.HouseholdId householdId, Set<Create.PersonDraft> personDraftSet) {
    }


}

