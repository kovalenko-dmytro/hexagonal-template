package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.CurrentUserOutputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.SignInOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthUserGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/auth_user_setup.yml")
    void testSignIn_success() {
        final var signInOutput = httpGraphQlTester
            .document("""
                    query {
                     signIn(
                         inputType: {
                           username: "username"
                           password: "password"
                         }
                       ) {
                         tokenType
                       	accessToken
                       	accessTokenExpired
                       }
                 }
                """)
            .execute()
            .errors()
            .verify()
            .path("signIn")
            .entity(SignInOutputType.class)
            .get();

        assertNotNull(signInOutput);
        assertNotNull(signInOutput.accessToken());
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/auth_user_setup.yml")
    void testSignIn_fail() {
        httpGraphQlTester
            .document("""
                    query {
                     signIn(
                         inputType: {
                           username: "username"
                           password: "wrong_password"
                         }
                       ) {
                         tokenType
                       	accessToken
                       	accessTokenExpired
                       }
                 }
                """)
            .execute()
            .errors()
            .expect((error) -> "UNAUTHORIZED".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/current_user_setup.yml")
    @WithMockUser(username = "current_user")
    void testGetCurrentUser_success() {
        final var currentUser = httpGraphQlTester
            .document("""
                    query {
                     getCurrentUser {
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
            .path("getCurrentUser")
            .entity(CurrentUserOutputType.class)
            .get();

        assertNotNull(currentUser);
        assertEquals("5a8d68c8-2f28-4b53-ac5a-2db586512444", currentUser.userId());
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/current_user_setup.yml")
    void testGetCurrentUser_fail() {
        httpGraphQlTester
            .document("""
                    query {
                     getCurrentUser {
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