package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateEmailPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateEmailPersistenceAdapter createEmailPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void createEmail_success() {
        final var expected = EmailsTestData.email();

        final var actual = createEmailPersistenceAdapter.createEmail(expected);

        assertNotNull(actual);
        assertNotNull(actual.getEmailId());
        assertEquals(expected.getSendBy(), actual.getSendBy());
        assertEquals(expected.getEmailType(), actual.getEmailType());
        assertEquals(expected.getEmailStatus(), actual.getEmailStatus());

        emailRepository.deleteAll();
    }
}