package dev.shantanu.money.tracker.person;

import dev.shantanu.money.tracker.common.Ids;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "money_tracker", name ="person")
public class Person {
    @Id
    private final Long personId;
    private final Long householdId;
    private final String name;
    private final String taxId;
    @Column("email")
    private final String email;
    @Column("date_of_birth")
    private final LocalDate dateOfBirth;
    @Column("created_datetime")
    private final LocalDateTime createdDateTime;
    @Column("updated_datetime")
    private final LocalDateTime updatedDateTime;

    public Person(Long personId, Long householdId,
                  String name, String taxId, String email, LocalDate dateOfBirth,
                  LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        this.personId = personId;
        this.householdId = householdId;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    public Long id() {
        return personId;
    }

    public Ids.HouseholdId getHouseholdId() {
        return new Ids.HouseholdId(householdId);
    }

    public String getName() {
        return name;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public LocalDateTime getUpdatedDateTime() {
        return updatedDateTime;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;

        Person person = (Person) object;
        return Objects.equals(personId, person.personId) && Objects.equals(householdId, person.householdId) && Objects.equals(name, person.name) && Objects.equals(taxId, person.taxId) && Objects.equals(email, person.email) && Objects.equals(dateOfBirth, person.dateOfBirth) && Objects.equals(createdDateTime, person.createdDateTime) && Objects.equals(updatedDateTime, person.updatedDateTime);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(personId);
        result = 31 * result + Objects.hashCode(householdId);
        result = 31 * result + Objects.hashCode(name);
        result = 31 * result + Objects.hashCode(taxId);
        result = 31 * result + Objects.hashCode(email);
        result = 31 * result + Objects.hashCode(dateOfBirth);
        result = 31 * result + Objects.hashCode(createdDateTime);
        result = 31 * result + Objects.hashCode(updatedDateTime);
        return result;
    }
}
