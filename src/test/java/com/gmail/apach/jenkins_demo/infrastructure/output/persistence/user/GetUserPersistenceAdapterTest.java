package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetUserPersistenceAdapterTest extends AbstractIntegrationTest {

    protected static final String USERNAME = "username";
    @Autowired
    private GetUserPersistenceAdapter getUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_user_by_username_setup.yml")
    void getByUsername() {
        final var actual = getUserPersistenceAdapter.getByUsername(USERNAME);

        assertTrue(actual.isPresent());
        assertEquals(USERNAME, actual.get().getUsername());
    }
}