package com.gmail.apach.jenkins_demo;

import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.redis.testcontainers.RedisContainer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

@SpringBootTest(classes = JenkinsDemoApplication.class)
@ActiveProfiles("test")
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected CacheManager cacheManager;

    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    protected static final RedisContainer REDIS_CONTAINER =
        new RedisContainer(DockerImageName.parse("redis:7.0.5-alpine"));
    protected static final RabbitMQContainer RABBIT_CONTAINER =
        new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"))
            .withReuse(true);

    static {
        POSTGRESQL_CONTAINER.start();
        REDIS_CONTAINER.start();
        RABBIT_CONTAINER.start();
    }

    @BeforeAll
    static void beforeAll() {
        System.setProperty("DB_URL", POSTGRESQL_CONTAINER.getJdbcUrl());
        System.setProperty("DB_USERNAME", POSTGRESQL_CONTAINER.getUsername());
        System.setProperty("DB_PASSWORD", POSTGRESQL_CONTAINER.getPassword());

        System.setProperty("spring.redis.host", REDIS_CONTAINER.getHost());
        System.setProperty("spring.redis.port", REDIS_CONTAINER.getFirstMappedPort().toString());

        System.setProperty("spring.rabbitmq.host", RABBIT_CONTAINER.getHost());
        System.setProperty("spring.rabbitmq.port", RABBIT_CONTAINER.getAmqpPort().toString());
        System.setProperty("spring.rabbitmq.username", RABBIT_CONTAINER.getAdminUsername());
        System.setProperty("spring.rabbitmq.password", RABBIT_CONTAINER.getAdminPassword());
    }

    @BeforeEach
    @DataSet("datasets/infrastructure/clear.yml")
    protected void beforeEach() {
    }

    @AfterEach
    protected void afterEach() {
        cacheManager.getCacheNames()
            .forEach(
                cacheName -> Optional
                    .ofNullable(cacheManager.getCache(cacheName))
                    .ifPresent(Cache::clear)
            );
    }

}
