package dev.shantanu.money.tracker.account;

import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
class AccountServiceTest implements ApplicationContextAware {
    public static final String PERSON_ACCOUNT_TABLE = "person_account";
    private static ApplicationContext context;
    private static final String SCHEMA_NAME = "money_tracker";
    private static final String PERSON_TABLE_NAME = "person";
    private final long householdId = 100001L;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        AccountServiceTest.context = applicationContext;
    }

    @Autowired
    private AccountService accountService;

    @Autowired
    JdbcTemplate jdbcTemplate;

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

    @AfterAll
    static void afterAll() {
        DataSource dataSource = context.getBean(DataSource.class);
        if (dataSource instanceof HikariDataSource) {
            ((HikariDataSource) dataSource).close();
        }
        postgres.stop();
    }

    @BeforeEach
    void beforeEach() {
        //insert householdId
        jdbcTemplate.update("INSERT INTO " + SCHEMA_NAME + "." + "household" + "(household_id, name) VALUES (?, ?)", householdId, "household name - " + householdId);
    }

    @AfterEach
    void afterEach() {
        //clean up
        String truncateTableSQL = Stream.of("account", "person_account", "person", "household")
                .map(tableName -> SCHEMA_NAME + "." + tableName)
                .collect(Collectors.joining(",", "TRUNCATE TABLE ", " CASCADE"));
        jdbcTemplate.update(truncateTableSQL);
    }


    static Stream<Arguments> createAccountScenarios() {
        return Stream.of(
                Arguments.of(1001L, "My Bank-1", "INDIVIDUAL", "ABBCE4030F", Map.of(20001L, 100.00), true),
                Arguments.of(1001L, "My Int Bank-1", "JOINT", "ABBCE4020F", Map.of(3001L, 50.00, 3002L, 50.00), true),
                Arguments.of(1000L, "My Int Bank-1", "JOINT", "ABBCE4010F", Map.of(3001L, 30.00, 3002L, 50.00, 3003L, 20.00), true)
        );
    }

    @ParameterizedTest
    @MethodSource("createAccountScenarios")
    void createAccount(Long accountId, String bankName, String accountTypeString, String taxId, Map<Long, Double> personOwnershipMap, boolean succesful) {
        personOwnershipMap = new LinkedHashMap<>(personOwnershipMap);
        AccountType accountType = AccountType.valueOf(accountTypeString);

        LinkedHashSet<Long> personIds = personOwnershipMap.keySet().stream().collect(LinkedHashSet::new, LinkedHashSet::add, LinkedHashSet::addAll);
        LinkedList<Double> owningPercentages = personOwnershipMap.values().stream().collect(LinkedList::new, LinkedList::add, LinkedList::addAll);

        assertEquals(personIds.size(), owningPercentages.size(), "Invalid Test: Number of ownership percentages should be equal to number of personIds from input");

        Set<AccountOwnership> accountOwnerships = buildAccountOwnerships(personOwnershipMap);

        CreateAccountCommand command = new CreateAccountCommand(new AccountDetail(accountId, accountType, bankName, taxId), accountOwnerships);

        //insert person id into person table
        final var insertQuery = "INSERT INTO " + SCHEMA_NAME + "." + PERSON_TABLE_NAME + " (person_id, household_id, name) VALUES (?, ?, ?)";
        personIds.forEach(idOfPerson -> jdbcTemplate.update(insertQuery, idOfPerson, householdId, "Person name of " + idOfPerson));

        Long accountCreated = accountService.createAccount(command);
        assertNotNull(accountCreated);

        Optional<AccountResult> accountResult = accountService.getAccountResult(accountCreated);

        assertTrue(accountResult.isPresent());
        assertEquals(bankName, accountResult.get().bankName());
        assertEquals(accountType, accountResult.get().accountType());
        record PersonAccount(Long accountId, Long personId, Double owningPercentage) {
        }
        RowMapper<PersonAccount> rowMapper = (resultSet, _) -> {
            var pId = resultSet.getLong("person_id");
            var aId = resultSet.getLong("account_id");
            var ownPercent = resultSet.getDouble("owning_percentage");
            return new PersonAccount(aId, pId, ownPercent);
        };

        List<PersonAccount> listOfAccountOwnership = jdbcTemplate.query("SELECT account_id, person_id, owning_percentage from " + SCHEMA_NAME + "." + PERSON_ACCOUNT_TABLE, rowMapper);
        Set<Long> savedPersonIds = listOfAccountOwnership.stream().map(PersonAccount::personId).collect(Collectors.toSet());
        Set<Long> savedAccount = listOfAccountOwnership.stream().map(PersonAccount::accountId).collect(Collectors.toSet());
        List<Double> savedOwningPerc = listOfAccountOwnership.stream().map(PersonAccount::owningPercentage).toList();

        Assertions.assertEquals(personIds, savedPersonIds);
        Assertions.assertEquals(owningPercentages.stream().sorted().toList(), savedOwningPerc.stream().sorted().toList());
        Assertions.assertTrue(owningPercentages.size() == savedOwningPerc.size()
                        && savedOwningPerc.containsAll(owningPercentages)
                        && owningPercentages.containsAll(savedOwningPerc),
                "The saved owning percentages should match the input owning percentages from AccountOwnership"
        );
        Assertions.assertEquals(accountId, savedAccount.stream().findFirst().get());


    }

    private static @NotNull Set<AccountOwnership> buildAccountOwnerships(Map<Long, Double> personOwnershipMap) {
        return personOwnershipMap.entrySet()
                .stream()
                .map(entry -> new AccountOwnership(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
    }
}