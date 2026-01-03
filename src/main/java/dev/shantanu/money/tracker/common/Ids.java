package dev.shantanu.money.tracker.common;

import java.math.BigDecimal;

public interface Ids {
    record HouseholdId(Long id) {
    }

    record PersonId(Long id) {
    }

    record AccountId(Long id) {
    }

    record StatementImportId(Long id) {
    }

    record TransactionId(Long id) {
    }

    record Money(BigDecimal amount, String currency) {
    }

}
