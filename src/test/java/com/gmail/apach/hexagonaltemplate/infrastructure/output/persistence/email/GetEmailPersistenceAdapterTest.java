package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class GetEmailPersistenceAdapterTest extends AbstractIntegrationTest {

    private static final String ABSENT_EMAIL_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512488";

    @Autowired
    private GetEmailPersistenceAdapter getEmailPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void getByEmailId_success() {
        final var email = emailRepository.save(EmailsTestData.emailEntity());

        final var actual = getEmailPersistenceAdapter.getByEmailId(email.getEmailId());

        assertNotNull(actual);
        assertEquals(email.getEmailId(), actual.getEmailId());

        emailRepository.deleteAll();
    }

    @Test
    void getByEmailId_notFound() {
        assertThrows(ResourceNotFoundException.class,
            () -> getEmailPersistenceAdapter.getByEmailId(ABSENT_EMAIL_ID));
    }
}