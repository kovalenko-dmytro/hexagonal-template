package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GetUserGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/get_user_by_id.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testGetByUserId_success() {
        final var user = httpGraphQlTester
            .document("""
                    query {
                     getByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440") {
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
            .path("getByUserId")
            .entity(UserOutputType.class)
            .get();

        assertNotNull(user);
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/get_user_by_id.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testGetByUserId_notFound() {
        httpGraphQlTester
            .document("""
                    query {
                     getByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512445") {
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
    @DataSet("datasets/infrastructure/input/graphql/user/get_user_by_id.yml")
    @WithMockUser(username = "user")
    void testGetByUserId_forbidden() {
        httpGraphQlTester
            .document("""
                    query {
                     getByUserId(userId: "5a8d68c8-2f28-4b53-ac5a-2db586512440") {
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