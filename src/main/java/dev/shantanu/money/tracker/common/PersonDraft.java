package dev.shantanu.money.tracker.common;

import java.time.LocalDate;

public record PersonDraft(
        String name,
        String taxId,
        String email,
        LocalDate dateOfBirth
) {
}