package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class DeleteFileGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/file/file_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/input/graphql/file/file_empty.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteByFileId_success() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByFileId(fileId: "file68c8-2f28-4b53-ac5a-2db586512441")
                    }
                """)
            .execute()
            .errors()
            .verify()
            .path("deleteByFileId");
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/file/file_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteByFileId_notFound() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByFileId(fileId: "file68c8-2f28-4b53-ac5a-2db586512455")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "NOT_FOUND".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/file/file_setup.yml")
    @WithMockUser(username = "user")
    void testDeleteByFileId_forbidden() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByFileId(fileId: "file68c8-2f28-4b53-ac5a-2db586512441")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "FORBIDDEN".contentEquals((String) error.getExtensions().get("classification")));
    }
}