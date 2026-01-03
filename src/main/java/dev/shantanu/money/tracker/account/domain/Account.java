package dev.shantanu.money.tracker.account.domain;

import dev.shantanu.money.tracker.account.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.shantanu.money.tracker.common.AppConstants.SCHEMA_NAME;

@Table(name = "account", schema = SCHEMA_NAME)
class Account extends AbstractAggregateRoot<Account> implements Persistable<Long> {
    @Id
    private final Long accountId;
    private final AccountType accountType;
    private final String bankName;
    private final String taxId;

    @MappedCollection(idColumn = "account_id")
    private Set<AccountOwnership> accountOwners;

    @Column("created_datetime")
    private final LocalDateTime createdDateTime;
    @Column("updated_datetime")
    private final LocalDateTime updatedDateTime;
    @Transient // This field is not mapped to the database
    private boolean isNew = true;

    // Domain constructor to create new account

    public Account(Long accountId, AccountType type, String bankName, String taxId, Set<AccountOwnership> accountOwners) {
        this.accountId = accountId; // Manual ID assignment
        this.accountType = type;
        this.bankName = bankName;
        this.taxId = taxId;
        if (accountOwners != null) {
            validateAccountOwnership(accountOwners);
        }
        this.accountOwners = accountOwners;
        this.createdDateTime = LocalDateTime.now();
        this.updatedDateTime = LocalDateTime.now();
        // isNew remains TRUE for new objects
    }

    private void validateAccountOwnership(Set<AccountOwnership> accountOwners) {
        Set<Double> percentages = accountOwners.stream().map(AccountOwnership::getOwnershipPercentage).collect(Collectors.toSet());
        Set<Long> personIds = accountOwners.stream().map(AccountOwnership::getPersonId).collect(Collectors.toSet());
        Objects.requireNonNull(personIds, "Require non null personIds entity to create AccountOwnership");

        switch (accountType) {
            case INDIVIDUAL -> {
                assert personIds.size() == 1 : "For AccountType INDIVIDUAL, only one personId is allowed";
                Long personId = personIds.iterator().next();
                Objects.requireNonNull(personId, "Require non null personId entity to create AccountOwnership");
            }
            case JOINT -> {
                double totalOwnershipPercentage = percentages.stream().mapToDouble(Double::doubleValue).sum();

                if (totalOwnershipPercentage <= 0 || totalOwnershipPercentage > 100) {
                    throw new IllegalArgumentException("Percentage must be between 0 and 100");
                }
            }
            case null, default -> throw new IllegalArgumentException("Account type not supported: " + accountType);
        }

    }

    // Persistence Constructor

    @PersistenceCreator
    public Account(Long accountId, AccountType accountType, String bankName,
                   String taxId, Set<AccountOwnership> accountOwners,
                   LocalDateTime createdDateTime,
                   LocalDateTime updatedDateTime) {
        this.accountId = accountId;
        this.accountType = accountType;
        this.bankName = bankName;
        this.taxId = taxId;
        this.accountOwners = accountOwners;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
        this.isNew = false;
    }

    public static AccountResult toAccountResult(Account account) {
        return new AccountResult(account.id(),
                account.getAccountType(),
                account.getBankName(),
                account.getTaxId(),
                account.getCreateDateTime(),
                account.getUpdateDateTime());
    }

    public String getTaxId() {
        return this.taxId;
    }

    public Long getAccountId() {
        return this.accountId;
    }

    public Long id() {
        return this.accountId;
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public Set<AccountOwnership> getAccountOwners() {
        return accountOwners;
    }

    public String getBankName() {
        return this.bankName;
    }

    public LocalDateTime getCreateDateTime() {
        return this.createdDateTime;
    }

    public LocalDateTime getUpdateDateTime() {
        return this.updatedDateTime;
    }

    @Override
    public Long getId() {
        return this.accountId;
    }

    @Override
    public boolean isNew() {
        return this.isNew;
    }

    static Account fromCreateCommand(CreateAccountCommand command) {
        AccountDetail accountDetail = command.accountDetail();
        return new Account(accountDetail.accountId(),
                accountDetail.accountType(),
                accountDetail.bankName(),
                accountDetail.taxId(), command.accountOwners());
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Account account = (Account) object;
        return Objects.equals(accountId, account.accountId)
                && accountType == account.accountType
                && Objects.equals(bankName, account.bankName)
                && Objects.equals(taxId, account.taxId)
                && Objects.equals(createdDateTime, account.createdDateTime)
                && Objects.equals(updatedDateTime, account.updatedDateTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(accountId);
        result = 31 * result + Objects.hashCode(accountType);
        result = 31 * result + Objects.hashCode(bankName);
        result = 31 * result + Objects.hashCode(taxId);
        result = 31 * result + Objects.hashCode(createdDateTime);
        result = 31 * result + Objects.hashCode(updatedDateTime);
        return result;
    }

    void setAccountOwners(Set<AccountOwnership> accountOwners) {
        this.accountOwners = accountOwners;
    }
}
