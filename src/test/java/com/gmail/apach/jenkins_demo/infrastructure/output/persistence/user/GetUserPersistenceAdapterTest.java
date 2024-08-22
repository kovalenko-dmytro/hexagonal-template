package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetUserPersistenceAdapterTest extends AbstractIntegrationTest {

    protected static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";
    @Autowired
    private GetUserPersistenceAdapter getUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_user.yml")
    void getByUsername() {
        final var actual = getUserPersistenceAdapter.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_user.yml")
    void getByUserId() {
        final var actual = getUserPersistenceAdapter.getByUserId(USER_ID);

        assertNotNull(actual);
        assertEquals(USER_ID, actual.getUserId());
    }
}