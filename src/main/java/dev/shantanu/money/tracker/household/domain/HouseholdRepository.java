package dev.shantanu.money.tracker.household.domain;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseholdRepository extends ListCrudRepository<Household, Long> {
}
