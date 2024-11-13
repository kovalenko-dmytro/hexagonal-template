package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteUserDbAdapterTest extends AbstractIntegrationTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @Autowired
    private DeleteUserDbAdapter deleteUserDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/user/delete_user_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/output/db/user/delete_user_expected.yml")
    void deleteByUserId() {
        deleteUserDbAdapter.deleteByUserId(USER_ID);
    }
}