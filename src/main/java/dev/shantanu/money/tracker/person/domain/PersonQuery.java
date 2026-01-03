package dev.shantanu.money.tracker.person.domain;

import dev.shantanu.money.tracker.common.Ids;

interface PersonQuery {
    record GetPersonByHousehold(Ids.HouseholdId householdId) {}
    record GetPersonById(Ids.PersonId personId) {}
}
