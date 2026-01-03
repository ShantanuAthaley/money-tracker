package dev.shantanu.money.tracker.common;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PersonResult(Ids.HouseholdId householdId,
                           Long personId,
                           String name,
                           String email,
                           String taxId,
                           LocalDate dateOfBirth,
                           LocalDateTime createdDateTime,
                           LocalDateTime updatedDateTime) {
}
