package com.gmail.apach.jenkins_demo;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.junit5.api.DBRider;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest(classes = JenkinsDemoApplication.class)
@ActiveProfiles("test")
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public abstract class AbstractIntegrationTest {

	protected static final PostgreSQLContainer<?> container =
		new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));

	static {
		container.start();
	}

	@BeforeAll
	static void beforeAll() {
		System.setProperty("DB_URL", container.getJdbcUrl());
		System.setProperty("DB_USERNAME", container.getUsername());
		System.setProperty("DB_PASSWORD", container.getPassword());
	}

}
