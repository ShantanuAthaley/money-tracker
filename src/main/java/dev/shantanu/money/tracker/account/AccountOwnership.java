package dev.shantanu.money.tracker.account;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

import static dev.shantanu.money.tracker.common.AppConstants.SCHEMA_NAME;

@Table(value = "person_account", schema = SCHEMA_NAME)
public class AccountOwnership {
    @Column("person_id")
    private Long personId;
    @Column("owning_percentage")
    private Double ownershipPercentage;
    @Transient
    private AccountType accountType;

    //Domain Constructor
    AccountOwnership(Long personId, Double ownershipPercentage) {
        this.personId = personId;
        this.ownershipPercentage = ownershipPercentage;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Double getOwnershipPercentage() {
        return ownershipPercentage;
    }

    public void setOwnershipPercentage(Double ownershipPercentage) {
        this.ownershipPercentage = ownershipPercentage;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccountOwnership that = (AccountOwnership) o;
        return Objects.equals(personId, that.personId) && Objects.equals(ownershipPercentage, that.ownershipPercentage) && accountType == that.accountType;
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(personId);
        result = 31 * result + Objects.hashCode(ownershipPercentage);
        result = 31 * result + Objects.hashCode(accountType);
        return result;
    }
}