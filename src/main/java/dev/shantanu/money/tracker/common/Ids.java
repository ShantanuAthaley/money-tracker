package dev.shantanu.money.tracker.common;

import java.math.BigDecimal;
import java.util.Random;

public interface Ids {
    static long randomLong() {
        var random = new Random();
        return random.nextLong(100, 1000);
    }

    record HouseholdId(Long id) {
    }

    record PersonId(Long id) {
        public static PersonId random() {
            return new PersonId(Ids.randomLong());
        }
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
