package dev.shantanu.money.tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.core.ApplicationModules;

//@Import(TestcontainersConfiguration.class)
@SpringBootTest
class MoneyTrackerApplicationTests {

	@Test
	void contextLoads() {
    ApplicationModules.of(MoneyTrackerApplication.class).verify();
	}

}
