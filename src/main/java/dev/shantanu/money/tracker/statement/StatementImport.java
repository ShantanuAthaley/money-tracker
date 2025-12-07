package dev.shantanu.money.tracker.statement;

import dev.shantanu.money.tracker.account.AccountType;
import dev.shantanu.money.tracker.common.Ids.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static dev.shantanu.money.tracker.common.Ids.AccountId;
import static dev.shantanu.money.tracker.common.Ids.StatementImportId;

@Table("ingress_statement")
class StatementImport {
    @Id
    private final StatementImportId statementImportId;
    private final LocalDateTime importDate;
    private final String statementFile;
    private final AggregateReference<AccountId, AccountId> accountId;
    private final AccountType accountType;
    private final Money openingBalance;
    private final Money closingBalance;
    private final List<String> parsingErrors;

    StatementImport(StatementImportId statementImportId, LocalDateTime importDate, String statementFile, AggregateReference<AccountId, AccountId> accountId, AccountType accountType, Money openingBalance, Money closingBalance, List<String> parsingErrors) {
        this.statementImportId = statementImportId;
        this.importDate = importDate;
        this.statementFile = statementFile;
        this.accountId = accountId;
        this.accountType = accountType;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.parsingErrors = parsingErrors;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        StatementImport that = (StatementImport) object;
        return Objects.equals(statementImportId, that.statementImportId) && Objects.equals(importDate, that.importDate) && Objects.equals(statementFile, that.statementFile) && Objects.equals(accountId, that.accountId) && accountType == that.accountType && Objects.equals(openingBalance, that.openingBalance) && Objects.equals(closingBalance, that.closingBalance) && Objects.equals(parsingErrors, that.parsingErrors);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(statementImportId);
        result = 31 * result + Objects.hashCode(importDate);
        result = 31 * result + Objects.hashCode(statementFile);
        result = 31 * result + Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(accountType);
        result = 31 * result + Objects.hashCode(openingBalance);
        result = 31 * result + Objects.hashCode(closingBalance);
        result = 31 * result + Objects.hashCode(parsingErrors);
        return result;
    }
}
