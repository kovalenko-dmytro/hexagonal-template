package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetUserDbAdapterTest extends AbstractIntegrationTest {

    protected static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";
    @Autowired
    private GetUserDbAdapter getUserDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_user.yml")
    void getByUsername() {
        final var actual = getUserDbAdapter.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_user.yml")
    void getByUserId() {
        final var actual = getUserDbAdapter.getByUserId(USER_ID);

        assertNotNull(actual);
        assertEquals(USER_ID, actual.getUserId());
    }
}