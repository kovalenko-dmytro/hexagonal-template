package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUserPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateUserPersistenceAdapter createUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/persistence/user/create_user_expected.yml",
        ignoreCols = {"user_", "created"})
    void createUser() {
        final var expected = User.builder()
            .username("username")
            .password("password")
            .firstName("first_name")
            .lastName("last_name")
            .created(LocalDateTime.now())
            .email("email@email")
            .enabled(true)
            .build();

        final var actual = createUserPersistenceAdapter.createUser(expected);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
    }
}