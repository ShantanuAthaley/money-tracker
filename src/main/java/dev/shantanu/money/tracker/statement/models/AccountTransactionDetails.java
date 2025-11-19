package dev.shantanu.money.tracker.statement.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Use records for your immutable data DTOs/Domain objects
public record AccountTransactionDetails(String accountNumber, List<AccountTransaction> transactions) {
}

record AccountTransaction(LocalDateTime dateTime, String mode, String particulars, BigDecimal deposit,
                          BigDecimal withdrawals, BigDecimal balance) {
}
