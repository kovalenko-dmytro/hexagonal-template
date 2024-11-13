package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class DeleteEmailGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/email/delete_email_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/input/graphql/email/delete_email_expected.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteByEmailId_success() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByEmailId(emailId: "eeed68c8-2f28-4b53-ac5a-2db586512441")
                    }
                """)
            .execute()
            .errors()
            .verify()
            .path("deleteByEmailId");
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/email/delete_email_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void testDeleteByEmailId_notFound() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByEmailId(emailId: "eeed68c8-2f28-4b53-ac5a-2db586512455")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "NOT_FOUND".contentEquals((String) error.getExtensions().get("classification")));
    }

    @Test
    @DataSet("datasets/infrastructure/input/graphql/email/delete_email_setup.yml")
    @WithMockUser(username = "user")
    void testDeleteByEmailId_forbidden() {
        httpGraphQlTester
            .document("""
                    mutation {
                      deleteByEmailId(emailId: "eeed68c8-2f28-4b53-ac5a-2db586512441")
                    }
                """)
            .execute()
            .errors()
            .expect((error) -> "FORBIDDEN".contentEquals((String) error.getExtensions().get("classification")));
    }
}