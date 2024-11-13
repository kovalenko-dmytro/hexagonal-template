package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.email;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

class GetEmailsGraphQlAdapterTest extends AbstractControllerIntegrationTest {

    @Test
    @DataSet("datasets/infrastructure/input/graphql/email/get_emails_setup.yml")
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER"})
    void testGetEmails_success() {
        final var emails = httpGraphQlTester
            .document("""
                    query {
                     getEmails(
                         sendBy: null
                         sendTo: null
                         dateSendFrom: null
                         dateSendTo: null
                         emailType: null
                         emailStatus: null
                         page: 1
                         size: 5
                         sort: ["sendBy"]
                       ) {
                         content {
                                __typename
                                ... on EmailOutputType {
                                  emailId
                                  sendBy
                                  sendTo
                                  cc
                                  subject
                                  sendTime
                                  emailType
                                  emailStatus
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
            .path("getEmails")
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
    @DataSet("datasets/infrastructure/input/graphql/email/get_emails_setup.yml")
    @WithMockUser(username = "user")
    void testGetEmails_forbidden() {
        httpGraphQlTester
            .document("""
                    query {
                     getEmails(
                         sendBy: null
                         sendTo: null
                         dateSendFrom: null
                         dateSendTo: null
                         emailType: null
                         emailStatus: null
                         page: 1
                         size: 5
                         sort: ["sendBy"]
                       ) {
                         content {
                                __typename
                                ... on EmailOutputType {
                                  emailId
                                  sendBy
                                  sendTo
                                  cc
                                  subject
                                  sendTime
                                  emailType
                                  emailStatus
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