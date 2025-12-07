package dev.shantanu.money.tracker.household.integration;

import dev.shantanu.money.tracker.common.Ids;
import dev.shantanu.money.tracker.household.HouseholdService;
import dev.shantanu.money.tracker.household.domain.Household;
import dev.shantanu.money.tracker.household.domain.HouseholdRepository;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCommands.Create.PersonDraft;
import dev.shantanu.money.tracker.household.domain.command.HouseholdCreatedResult;
import dev.shantanu.money.tracker.household.domain.query.HouseholdQueries;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextAware;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class HouseholdIntegrationTest implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("personal_finance")
            .withUsername("postgres")
            .withPassword("postgres");


    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);

        r.add("spring.flyway.enabled", () -> "true");
        r.add("spring.flyway.url", postgres::getJdbcUrl);
        r.add("spring.flyway.user", postgres::getUsername);
        r.add("spring.flyway.password", postgres::getPassword);
    }

    @Autowired
    HouseholdService householdService;

    @Autowired
    HouseholdRepository householdRepository;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        DataSource dataSource = context.getBean(DataSource.class);
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
        postgres.stop();
    }

    @Test
    void shouldSaveAndRetrieveHousehold() {
        // Given
        final String familyName = "Athaley";
        HouseholdCommands.Create athaleys = buildAthaleysCreateFamilyCommand(familyName);
        // When
        HouseholdCreatedResult houseHold = householdService.createHouseHold(athaleys);

        // Then
        assertAll(
                "household created result",
                () -> assertThat(houseHold).isNotNull(),
                () -> assertThat(houseHold.householdName()).isEqualTo(familyName),
                () -> assertThat(houseHold.personIds()).hasSize(3));

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
                new PersonDraft("Madhukar YELAHTA", "ASSPW3035F", "member1@family.fam", LocalDate.of(2000, Month.JUNE, 26)),
                new PersonDraft("Sudha YELAHTA", "ASSPW3036F", "member2@family.fam", LocalDate.of(2002, Month.FEBRUARY, 6)),
                new PersonDraft("Anay YELAHTA", "ASSPW3037F", "Family@family.fam", LocalDate.of(2015, Month.MARCH, 23))
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
        Optional<HouseholdCreatedResult> householdCreatedResult = householdService.addMembers(addMembers);

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
