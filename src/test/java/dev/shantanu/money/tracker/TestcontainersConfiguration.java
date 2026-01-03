package dev.shantanu.money.tracker;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

	@Bean
	@ServiceConnection
	static PostgreSQLContainer<?> postgresContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
	}

	@DynamicPropertySource
	static void props(DynamicPropertyRegistry r) {
		PostgreSQLContainer<?> postgres = postgresContainer();
		r.add("spring.datasource.url", postgres::getJdbcUrl);
		r.add("spring.datasource.username", postgres::getUsername);
		r.add("spring.datasource.password", postgres::getPassword);

		r.add("spring.flyway.enabled", () -> "true");
		r.add("spring.flyway.url", postgres::getJdbcUrl);
		r.add("spring.flyway.user", postgres::getUsername);
		r.add("spring.flyway.password", postgres::getPassword);
	}

}
