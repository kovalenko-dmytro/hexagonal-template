package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

class GetFilesGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/file/get_files_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER"})
    void testGetFiles_success() {
        final var emails = httpGraphQlTester
            .document("""
                    query {
                     getFiles(
                         fileName: null
                         createdFrom: null
                         createdTo: null
                         page: 1
                         size: 5
                         sort: ["created"]
                       ) {
                         content {
                                __typename
                                ... on FileOutputType {
                                  fileId
                                  storageKey
                                  fileName
                                  contentType
                                  size
                                  created
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
            .path("getFiles")
            .entity(PageOutputType.class)
            .get();

        assertNotNull(emails);
        assertNotNull(emails.content());
        assertFalse(emails.content().isEmpty());
        assertEquals(5, emails.content().size());
        assertEquals(7, emails.totalElements());
        assertEquals(2, emails.totalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/file/get_files_setup.yml")
    @WithMockUser(username = "user")
    void testGetFiles_forbidden() {
        httpGraphQlTester
            .document("""
                    query {
                     getFiles(
                         fileName: null
                         createdFrom: null
                         createdTo: null
                         page: 1
                         size: 5
                         sort: ["created"]
                       ) {
                         content {
                                __typename
                                ... on FileOutputType {
                                  fileId
                                  storageKey
                                  fileName
                                  contentType
                                  size
                                  created
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