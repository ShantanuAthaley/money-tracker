package dev.shantanu.money.tracker.household.domain;

import com.zaxxer.hikari.HikariDataSource;
import dev.shantanu.money.tracker.TestcontainersConfiguration;
import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.common.PersonDraft;
import dev.shantanu.money.tracker.household.HouseholdService;
import dev.shantanu.money.tracker.household.contract.HouseholdCommands;
import dev.shantanu.money.tracker.household.contract.HouseholdQueries;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.shantanu.money.tracker.common.AppConstants.SCHEMA_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {
        TestcontainersConfiguration.class,
})

class HouseholdServiceTest implements ApplicationContextAware {
    private static final String HOUSEHOLD = "household";
    private static final String PERSON_TABLE = "person";
    private static ApplicationContext context;

    @Autowired
    private HouseholdService householdService;
    @Autowired
    private HouseholdRepository householdRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) {
        assertNotNull(applicationContext, "context is null");
        context = applicationContext;
    }

    @AfterAll
    static void afterAll() {
        DataSource dataSource = context.getBean(DataSource.class);
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
        context.getBean(PostgreSQLContainer.class).stop();
    }

    @Test
    void shouldSaveAndRetrieveHousehold() {
        // Given
        final String familyName = "Athaley";
        HouseholdCommands.Create athaleys = buildAthaleysCreateFamilyCommand(familyName);
        // When
        HouseholdCommands.HouseholdCreatedResult houseHold = householdService.createHouseHold(athaleys);

        record HouseholdCreatedResult(Long householdId, String name) {
        }
        List<HouseholdCreatedResult> householdResult = jdbcTemplate.query("SELECT household_id, name from " + SCHEMA_NAME + "." + HOUSEHOLD,
                (resultSet, _)
                        -> new HouseholdCreatedResult(resultSet.getLong("household_id"),
                        resultSet.getString("name")));

        record PersonCreatedResult(Long householdId, Long personId, String name) {
        }
        List<PersonCreatedResult> personCreatedResults = jdbcTemplate.query("SELECT household_id, person_id, name from " + SCHEMA_NAME + "." + PERSON_TABLE,
                (resultSet, _) ->
                        new PersonCreatedResult(resultSet.getLong("household_id"),
                                resultSet.getLong("person_id"),
                                resultSet.getString("name")));

        // Then
        var household = householdResult.stream().findFirst().orElseThrow(AssertionError::new);
        assertAll(
                "household created result",
                () -> assertThat(household).isNotNull(),
                () -> assertThat(household.name).isEqualTo(familyName),
                () -> assertThat(personCreatedResults).hasSize(3));

        Set<Ids.PersonId> allPersonIds = householdService
                .getAllPersonIds(new HouseholdQueries.GetMembers(houseHold.householdId()));

        assertAll(
                "all person ids",
                () -> assertThat(allPersonIds).isNotNull(),
                () -> assertThat(allPersonIds).hasSize(3),
                () -> assertThat(allPersonIds).first().isNotNull(),
                () -> assertThat(allPersonIds).last().isNotNull());

    }

    private static HouseholdCommands.@NotNull Create buildAthaleysCreateFamilyCommand(String familyName) {
        Set<PersonDraft> personDrafts = Set.of(
                new PersonDraft("UNATANASH YELAHTA", "ASSPW3035F", "member1@family.fam", LocalDate.of(2000, Month.JUNE, 26)),
                new PersonDraft("AHDNAAGIHSIN YELAHTA", "ASSPW3036F", "member2@family.fam", LocalDate.of(2002, Month.FEBRUARY, 6)),
                new PersonDraft("YANA YELAHTA", "ASSPW3037F", "Family@family.fam", LocalDate.of(2015, Month.MARCH, 23))
        );
        return new HouseholdCommands.Create(familyName, personDrafts);
    }

    @Test
    void shouldUpdateHouseholdMembers() {
        // Given
        Household household = householdRepository.save(Household.createWithName("Update Test"));
        var personDraft = new PersonDraft("Test Member", "TEST00131F", "test@unit.fam", LocalDate.of(1980, Month.JANUARY, 01));
        HouseholdCommands.AddMembers addMembers = new HouseholdCommands.AddMembers(household.getHouseholdId(), Set.of(personDraft));

        // When
        Optional<HouseholdCommands.HouseholdCreatedResult> householdCreatedResult = householdService.addMembers(addMembers);

        // Then
        var updatedWithMember = householdRepository.findById(household.getHouseholdId().id());

        assertAll(
                "household update results",
                () -> assertThat(householdCreatedResult).isPresent(),
                () -> assertThat(householdCreatedResult).hasValueSatisfying(
                        result -> assertThat(result.personIds()).hasSize(1)),
                () -> assertThat(updatedWithMember).isPresent(),
                () -> assertThat(updatedWithMember).hasValueSatisfying(
                        h -> assertThat(h.getName()).isEqualTo("Update Test")));
    }

    @Test
    void shouldDeleteHousehold() {
        // Given
        Household household = householdRepository.save(Household.createWithName("To Be Deleted"));

        // When
        householdRepository.delete(household);
        var existsAfterDelete = householdRepository.existsById(household.getHouseholdId().id());

        // Then
        assertThat(existsAfterDelete).isFalse();
    }
}
