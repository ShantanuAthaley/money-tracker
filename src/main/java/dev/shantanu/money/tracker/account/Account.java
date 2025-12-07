package dev.shantanu.money.tracker.account;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static dev.shantanu.money.tracker.common.Ids.AccountId;
import static dev.shantanu.money.tracker.common.Ids.PersonId;

@Table("account")
class Account {
    @Id
    private final AccountId accountId;
    private final AccountType accountType;
    private final String bankName;
    @MappedCollection(idColumn = "person_id")
    private final Set<PersonId> personIds;
    private final String taxId;
    private final LocalDateTime createdDateTime;
    private final LocalDateTime updatedDateTime;

    public Account(AccountId accountId, AccountType accountType, String bankName,
                   Set<PersonId> personIds, String taxId,
                   LocalDateTime createdDateTime,
                   LocalDateTime updatedDateTime) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.bankName = bankName;
        this.personIds = personIds != null ? personIds : Set.of();
        this.taxId = taxId;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public String getTaxId() {
        return this.taxId;
    }

    public AccountId getAccountId() {
        return this.accountId;
    }

    public Long id() {
        return this.accountId.id();
    }

    public Set<PersonId> personIds() {
        return this.personIds;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public String getBankName() {
        return this.bankName;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Account account = (Account) object;
        return Objects.equals(accountId, account.accountId) && accountType == account.accountType && Objects.equals(bankName, account.bankName) && Objects.equals(personIds, account.personIds) && Objects.equals(taxId, account.taxId) && Objects.equals(createdDateTime, account.createdDateTime) && Objects.equals(updatedDateTime, account.updatedDateTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(accountType);
        result = 31 * result + Objects.hashCode(bankName);
        result = 31 * result + Objects.hashCode(personIds);
        result = 31 * result + Objects.hashCode(taxId);
        result = 31 * result + Objects.hashCode(createdDateTime);
        result = 31 * result + Objects.hashCode(updatedDateTime);
        return result;
    }
}
