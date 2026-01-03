package dev.shantanu.money.tracker.household.domain;

import dev.shantanu.money.tracker.common.Ids;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Table(schema = "money_tracker", name = "household")
class Household {
    @Id
    private final Long householdId;
    private final String name;

    public static Household createWithName(String name) {
        return new Household(null, name);
    }

    public Ids.HouseholdId getHouseholdId() {
        return new Ids.HouseholdId(householdId);
    }

    public String getName() {
        return name;
    }

    Household(Long householdId,
              @NotNull String name) {
        this.householdId = householdId;
        this.name = name;

    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(householdId);
        result = 31 * result + Objects.hashCode(name);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Household household = (Household) object;
        return Objects.equals(householdId, household.householdId) && Objects.equals(name, household.name);
    }

}
