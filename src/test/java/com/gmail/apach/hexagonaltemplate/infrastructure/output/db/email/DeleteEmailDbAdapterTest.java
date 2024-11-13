package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteEmailDbAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private DeleteEmailDbAdapter deleteEmailDbAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void deleteByEmailId_success() {
        final var email = emailRepository.save(EmailsTestData.emailEntity());

        deleteEmailDbAdapter.deleteByEmailId(email.getEmailId());

        final var actual = emailRepository.findById(email.getEmailId());

        assertTrue(actual.isEmpty());
    }
}