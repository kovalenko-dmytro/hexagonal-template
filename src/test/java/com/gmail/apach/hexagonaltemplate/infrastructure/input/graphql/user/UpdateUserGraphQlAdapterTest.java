package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/update_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/graphql/user/update_user_expected.yml",
        ignoreCols = {"user_", "role_", "created", "password"})
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testUpdateUser_success() {
        final var user = httpGraphQlTester
            .document("""
                    mutation {
                    updateUser(
                      userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440"
                      inputType: { firstName: "first_name_new", lastName: "last_name_new", enabled: false }
                    ) {
                      userId
                      username
                      firstName
                      lastName
                      email
                      enabled
                      created
                      createdBy
                      roles
                    }
                  }
                """)
            .execute()
            .errors()
            .verify()
            .path("updateUser")
            .entity(UserOutputType.class)
            .get();

        assertNotNull(user);
        assertEquals("first_name_new", user.firstName());
        assertEquals("last_name_new", user.lastName());
        assertFalse(user.enabled());
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/update_user_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testUpdateUser_badRequest() {
        httpGraphQlTester
            .document("""
                    mutation {
                      updateUser(
                        userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440"
                        inputType: { roleType: ADMIN }
                      ) {
                        userId
                        username
                        firstName
                        lastName
                        email
                        enabled
                        created
                        createdBy
                        roles
                      }
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "ValidationError".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/update_user_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testUpdateUser_notFound() {
        httpGraphQlTester
            .document("""
                    mutation {
                    updateUser(
                      userId: "5a8d68c8-2f28-4b53-ac5a-2db586512445"
                      inputType: { firstName: "aaa_updated", lastName: "aaa_updated", roleType: MANAGER }
                    ) {
                      userId
                      username
                      firstName
                      lastName
                      email
                      enabled
                      created
                      createdBy
                      roles
                    }
                  }
                """)
            .execute()
            .errors()
            .expect((error) -> "NOT_FOUND".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/update_user_setup.yml")
    @WithMockUser(username = "user")
    void testUpdateUser_forbidden() {
        httpGraphQlTester
            .document("""
                    mutation {
                    updateUser(
                      userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440"
                      inputType: { firstName: "aaa_updated", lastName: "aaa_updated", roleType: MANAGER }
                    ) {
                      userId
                      username
                      firstName
                      lastName
                      email
                      enabled
                      created
                      createdBy
                      roles
                    }
                  }
                """)
            .execute()
            .errors()
            .expect((error) -> "FORBIDDEN".contentEquals((String) error.getExtensions().get("classification")));
    }
}