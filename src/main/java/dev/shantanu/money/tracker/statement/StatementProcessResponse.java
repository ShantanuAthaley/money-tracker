package dev.shantanu.money.tracker.statement;

import java.time.LocalDate;

public record StatementProcessResponse(String fileName, LocalDate from, LocalDate to,
                                       int transactionCount) {
}