package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

class GetUsersGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/get_users_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testGetUsers_success() {
        final var users = httpGraphQlTester
            .document("""
                    query {
                     getUsers(
                         username: null
                         firstName: null
                         lastName: null
                         email: null
                         enabled: null
                         createdFrom: null
                         createdTo: null
                         createdBy: null
                         page: 1
                         size: 5
                         sort: ["created"]
                       ) {
                         content {
                                __typename
                                ... on UserOutputType {
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
                         size
                         number
                         totalElements
                         totalPages
                       }
                 }
                """)
            .execute()
            .errors()
            .verify()
            .path("getUsers")
            .entity(PageOutputType.class)
            .get();

        assertNotNull(users);
        assertNotNull(users.content());
        assertFalse(users.content().isEmpty());
        assertEquals(5, users.content().size());
        assertEquals(7, users.totalElements());
        assertEquals(2, users.totalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/user/get_users_setup.yml")
    @WithMockUser(username = "user")
    void testGetUsers_forbidden() {
        httpGraphQlTester
            .document("""
                    query {
                     getUsers(
                         username: null
                         firstName: null
                         lastName: null
                         email: null
                         enabled: null
                         createdFrom: null
                         createdTo: null
                         createdBy: null
                         page: 1
                         size: 5
                         sort: ["created"]
                       ) {
                         content {
                                __typename
                                ... on UserOutputType {
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
                         size
                         number
                         totalElements
                         totalPages
                       }
                 }
                """)
            .execute()
            .errors()
            .expect((error) -> "FORBIDDEN".contentEquals((String) error.getExtensions().get("classification")));
    }
}