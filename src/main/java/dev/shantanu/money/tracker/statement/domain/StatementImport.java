package dev.shantanu.money.tracker.statement.domain;

import dev.shantanu.money.tracker.account.AccountType;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.jdbc.core.mapping.AggregateReference;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static dev.shantanu.money.tracker.common.AppConstants.SCHEMA_NAME;
import static dev.shantanu.money.tracker.common.Ids.AccountId;

@Table(value = "statement_import", schema = SCHEMA_NAME)
class StatementImport {
    @Id
    private final Long statementImportId;
    private final LocalDateTime importDate;
    private final String statementFile;
    private final AggregateReference<AccountId, Long> accountId;
    private final AccountType accountType;
    private final Double openingBalance;
    private final Double closingBalance;
    private final List<String> parsingErrors;
    private final String importStatus;

    // Domain constructor
    StatementImport(Long statementImportId, LocalDateTime importDate, String statementFile,
                    AccountId accountId, AccountType accountType,
                    Double openingBalance, Double closingBalance,
                    List<String> parsingErrors) {
        this.statementImportId = statementImportId;
        this.importDate = importDate;
        this.statementFile = statementFile;
        this.accountId = AggregateReference.to(accountId.id()); //It is expected to receive input as AccountId
        this.accountType = accountType;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.parsingErrors = parsingErrors;
        this.importStatus = !parsingErrors.isEmpty() ? "PARSED" : "NEEDS_REVIEW";
    }

    @PersistenceCreator
    StatementImport(Long statementImportId, LocalDateTime importDate, String statementFile,
                    Long accountId, AccountType accountType,
                    Double openingBalance, Double closingBalance, List<String> parsingErrors, String importStatus) {
        this. statementImportId = statementImportId;
        this.importDate = importDate;
        this.statementFile = statementFile;
        this.accountId = AggregateReference.to(accountId);
        this.accountType = accountType;
        this.openingBalance = openingBalance;
        this.closingBalance = closingBalance;
        this.parsingErrors = parsingErrors;
        this.importStatus = importStatus;
    }

    public String getImportStatus() {
        return importStatus;
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
