package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateUserGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/graphql/user/create_user_expected.yml",
        ignoreCols = {"user_", "role_", "created", "password"})
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testCreateUser_success() {
        final var user = httpGraphQlTester
            .document("""
                    mutation {
                     createUser(
                       inputType: {
                         username: "user"
                         password: "user_password"
                         firstName: "first_name"
                         lastName: "last_name"
                         email: "email@email"
                         roleType: USER
                       }
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
            .path("createUser")
            .entity(UserOutputType.class)
            .get();

        assertNotNull(user);
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/create_user_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testCreateUser_badRequest() {
        httpGraphQlTester
            .document("""
                    mutation {
                     createUser(
                       inputType: {
                         username: ""
                         password: "short"
                         firstName: "first_name"
                         lastName: "last_name"
                         email: "email@email"
                         roleType: ADMIN
                       }
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
    @DataSet("datasets/infrastructure/input/graphql/user/create_user_setup.yml")
    @WithMockUser(username = "user")
    void testCreateUser_forbidden() {
        httpGraphQlTester
            .document("""
                    mutation {
                     createUser(
                       inputType: {
                         username: "user"
                         password: "user_password"
                         firstName: "first_name"
                         lastName: "last_name"
                         email: "email@email"
                         roleType: USER
                       }
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