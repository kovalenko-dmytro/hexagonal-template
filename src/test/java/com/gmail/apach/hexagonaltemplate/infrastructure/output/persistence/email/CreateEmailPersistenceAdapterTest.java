package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class CreateEmailPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateEmailPersistenceAdapter createEmailPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void createEmail_success() {
        final var expected = EmailsTestData.email();

        createEmailPersistenceAdapter.createEmail(expected);
        final var actual = emailRepository.findBySendBy(expected.getSendBy());

        assertTrue(actual.isPresent());
        final var savedEmail = actual.get();
        assertNotNull(savedEmail.getEmailId());
        assertEquals(expected.getSendBy(), savedEmail.getSendBy());
        assertEquals(expected.getEmailType(), savedEmail.getEmailType());
        assertEquals(expected.getEmailStatus(), savedEmail.getEmailStatus());

        emailRepository.deleteAll();
    }
}