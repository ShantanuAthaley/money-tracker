package dev.shantanu.money.tracker.account;

import java.time.LocalDateTime;

public record AccountResult(Long id,
                            AccountType accountType,
                            String bankName,
                            String taxId,
                            LocalDateTime createdDateTime,
                            LocalDateTime updatedDateTime) {
}
