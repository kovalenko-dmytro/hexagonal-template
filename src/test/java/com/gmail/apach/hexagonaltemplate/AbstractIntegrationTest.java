package com.gmail.apach.hexagonaltemplate;

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
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.util.Optional;

@SpringBootTest(classes = HexagonalTemplateApplication.class)
@ActiveProfiles("test")
@DBRider
@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE)
public abstract class AbstractIntegrationTest {

    private static final String BUCKET_NAME = "test-bucket";

    @Autowired
    protected CacheManager cacheManager;

    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
        new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"));
    protected static final RedisContainer REDIS_CONTAINER =
        new RedisContainer(DockerImageName.parse("redis:7.0.5-alpine"));
    protected static final RabbitMQContainer RABBIT_CONTAINER =
        new RabbitMQContainer(DockerImageName.parse("rabbitmq:3-management"));
    protected static final LocalStackContainer LOCAL_STACK_CONTAINER =
        new LocalStackContainer(DockerImageName.parse("localstack/localstack"))
            .withCopyFileToContainer(
                MountableFile.forClasspathResource("script/init-s3-bucket.sh"),
                "/etc/localstack/init/ready.d/init-s3-bucket.sh")
            .withServices(LocalStackContainer.Service.S3)
            .waitingFor(Wait.forLogMessage(".*Executed init-s3-bucket.sh.*", 1));

    protected static final GenericContainer<?> EMAIL_CONTAINER =
        new GenericContainer<>(DockerImageName.parse("greenmail/standalone:1.6.1"))
            .waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
            .withEnv(
                "GREENMAIL_OPTS",
                "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=user:password")
            .withExposedPorts(3025);


    static {
        POSTGRESQL_CONTAINER.start();
        REDIS_CONTAINER.start();
        RABBIT_CONTAINER.start();
        LOCAL_STACK_CONTAINER.start();
        EMAIL_CONTAINER.start();
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

        System.setProperty("spring.cloud.aws.credentials.access-key", LOCAL_STACK_CONTAINER.getAccessKey());
        System.setProperty("spring.cloud.aws.credentials.secret-key", LOCAL_STACK_CONTAINER.getSecretKey());
        System.setProperty("spring.cloud.aws.s3.region", LOCAL_STACK_CONTAINER.getRegion());
        System.setProperty("spring.cloud.aws.s3.endpoint", String.valueOf(LOCAL_STACK_CONTAINER.getEndpoint()));
        System.setProperty("aws.s3.bucket-name", BUCKET_NAME);

        System.setProperty("spring.mail.host", EMAIL_CONTAINER.getHost());
        System.setProperty("spring.mail.port", String.valueOf(EMAIL_CONTAINER.getFirstMappedPort()));
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
