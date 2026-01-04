package dev.shantanu.money.tracker.account;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Objects;

import static dev.shantanu.money.tracker.common.AppConstants.SCHEMA_NAME;

@Table(value = "person_account", schema = SCHEMA_NAME)
public class AccountOwnership {
    @Column("person_id")
    private final Long personId;
    @Column("owning_percentage")
    private final Double ownershipPercentage;

    //Domain Constructor
    AccountOwnership(Long personId, Double ownershipPercentage) {
        this.personId = personId;
        this.ownershipPercentage = ownershipPercentage;
    }

    public Long getPersonId() {
        return personId;
    }


    public Double getOwnershipPercentage() {
        return ownershipPercentage;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        AccountOwnership that = (AccountOwnership) o;
        return Objects.equals(personId, that.personId)
                && Objects.equals(ownershipPercentage, that.ownershipPercentage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(personId);
        result = 31 * result + Objects.hashCode(ownershipPercentage);
        return result;
    }
}