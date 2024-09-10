package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email;

import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.data.EmailsTestData;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteEmailPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private DeleteEmailPersistenceAdapter deleteEmailPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void deleteByEmailId_success() {
        final var email = emailRepository.save(EmailsTestData.emailEntity());

        deleteEmailPersistenceAdapter.deleteByEmailId(email.getEmailId());

        final var actual = emailRepository.findById(email.getEmailId());

        assertTrue(actual.isEmpty());
    }
}