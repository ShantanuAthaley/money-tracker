package dev.shantanu.money.tracker.person.domain;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PersonRepository extends ListCrudRepository<Person, Long> {
    @Query("SELECT person_id FROM money_tracker.person WHERE household_id = :householdId")
    Set<Long> getAllPersonIdByHouseholdId(Long householdId);
    Set<Person> getAllByHouseholdId(Long householdID);
    @Query("SELECT p.* FROM money_tracker.person p WHERE p.person_id = :personId")
    Optional<Person> getByPersonId(Long personId);
}
