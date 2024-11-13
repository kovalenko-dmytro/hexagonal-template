package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class DeleteUserGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/delete_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/graphql/user/delete_user_expected.yml",
        ignoreCols = {"user_", "role_", "created", "password"})
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteUser_success() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440")
                    }
                """)
            .execute()
            .errors()
            .verify()
            .path("deleteByUserId");
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/delete_user_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteUser_notFound() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512445")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "NOT_FOUND".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/delete_user_setup.yml")
    @WithMockUser(username = "user")
    void testDeleteUser_forbidden() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "FORBIDDEN".contentEquals((String) error.getExtensions().get("classification")));
    }
}