package dev.shantanu.money.tracker;

import org.springframework.boot.SpringApplication;

public class TestMoneyTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.from(MoneyTrackerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

