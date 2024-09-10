package com.gmail.apach.jenkins_demo.infrastructure.input.rest.email;

import com.gmail.apach.jenkins_demo.AbstractControllerIntegrationTest;
import com.gmail.apach.jenkins_demo.data.EmailsTestData;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GetEmailsRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/emails";

    @Autowired
    private EmailRepository emailRepository;

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    void getEmailsByAdmin_success() throws Exception {
        emailRepository.saveAll(EmailsTestData.emails());

        final var result = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var resultFromCache = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();

        assertEquals(HttpStatus.OK.value(), resultFromCache.getResponse().getStatus());

        emailRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void getEmailsByUser_forbidden() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}