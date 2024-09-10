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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

class DeleteEmailRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/emails/";
    private static final String INCORRECT_EMAIL_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512444";

    @Autowired
    private EmailRepository emailRepository;

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    void deleteByEmailId_success() throws Exception {
        final var email = emailRepository.save(EmailsTestData.emailEntity());

        final var result = mvc.perform(
                delete(BASE_PATH + email.getEmailId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());

        emailRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    void deleteByEmailId_notFound() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + INCORRECT_EMAIL_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deleteByEmailId_forbidden() throws Exception {
        final var email = emailRepository.save(EmailsTestData.emailEntity());

        final var result = mvc.perform(
                delete(BASE_PATH + email.getEmailId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());

        emailRepository.deleteAll();
    }
}