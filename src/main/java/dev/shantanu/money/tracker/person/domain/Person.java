package dev.shantanu.money.tracker.person.domain;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonDraft;
import dev.shantanu.money.tracker.common.PersonResult;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(schema = "money_tracker", name = "person")
class Person extends AbstractAggregateRoot<Person> {
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


    //[*] Spring Data JDBC will utilize this constructor as preferred way to construct the Person entity.
    @PersistenceCreator
    private Person(Long personId, Long householdId, String name, String taxId, String email, LocalDate dateOfBirth,
                   LocalDateTime createdDateTime, LocalDateTime updatedDateTime) {
        Objects.requireNonNull(householdId, "householdId must not be null");
        Objects.requireNonNull(name, "name must not be null");
        this.householdId = householdId;
        this.personId = personId;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdDateTime = createdDateTime;
        this.updatedDateTime = updatedDateTime;
    }

    // Domain constructor
    private Person(Long personId, Long householdId,
                   String name, String taxId, String email, LocalDate dateOfBirth) {
        Objects.requireNonNull(householdId, "householdId must not be null");
        this.householdId = householdId;
        this.personId = personId;
        this.name = name;
        this.taxId = taxId;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.createdDateTime = LocalDateTime.now();
        this.updatedDateTime = LocalDateTime.now();
    }

    public Long id() {
        return personId;
    }

    public Ids.HouseholdId getHouseholdId() {
        return new Ids.HouseholdId(householdId);
    }

    static PersonResult toPersonResult(Person person) {
        Objects.requireNonNull(person, "Require non null person entity to produce PersonResult");
        return new PersonResult(person.getHouseholdId(),
                person.personId,
                person.name,
                person.taxId,
                person.taxId,
                person.dateOfBirth,
                person.createdDateTime,
                person.updatedDateTime
        );
    }

    static Person buildFrom(Ids.HouseholdId householdId, PersonDraft personDraft) {
        return new Person(null, householdId.id(),
                personDraft.name(),
                personDraft.email(),
                personDraft.taxId(),
                personDraft.dateOfBirth());
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
