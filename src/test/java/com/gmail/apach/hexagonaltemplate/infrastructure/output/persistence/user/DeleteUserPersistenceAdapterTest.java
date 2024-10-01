package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteUserPersistenceAdapterTest extends AbstractIntegrationTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @Autowired
    private DeleteUserPersistenceAdapter deleteUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/delete_user_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/output/persistence/user/delete_user_expected.yml")
    void deleteByUserId() {
        deleteUserPersistenceAdapter.deleteByUserId(USER_ID);
    }
}