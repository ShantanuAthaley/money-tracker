package dev.shantanu.money.tracker.statement;

import dev.shantanu.money.tracker.common.Ids.AccountId;
import dev.shantanu.money.tracker.common.Ids.TransactionId;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;

import java.time.LocalDate;
import java.util.Objects;

class StatementTransactionStaging {

    @Id
    private TransactionId id;

    private AggregateReference<AccountId, AccountId> accountId;

    private LocalDate transactionDate;
    private String description;
    private String category;

    private java.math.BigDecimal amount;
    private java.math.BigDecimal balance;

    private String error;

    public StatementTransactionStaging(TransactionId id,
                                       AggregateReference<AccountId, AccountId> accountId,
                                       LocalDate transactionDate,
                                       String description,
                                       String category,
                                       java.math.BigDecimal amount,
                                       java.math.BigDecimal balance,
                                       String error) {
        this.id = id;
        this.accountId = accountId;
        this.transactionDate = transactionDate;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.balance = balance;
        this.error = error;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        StatementTransactionStaging that = (StatementTransactionStaging) object;
        return Objects.equals(id, that.id) && Objects.equals(accountId, that.accountId) && Objects.equals(transactionDate, that.transactionDate) && Objects.equals(description, that.description) && Objects.equals(category, that.category) && Objects.equals(amount, that.amount) && Objects.equals(balance, that.balance) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(id);
        result = 31 * result + Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(transactionDate);
        result = 31 * result + Objects.hashCode(description);
        result = 31 * result + Objects.hashCode(category);
        result = 31 * result + Objects.hashCode(amount);
        result = 31 * result + Objects.hashCode(balance);
        result = 31 * result + Objects.hashCode(error);
        return result;
    }
}
