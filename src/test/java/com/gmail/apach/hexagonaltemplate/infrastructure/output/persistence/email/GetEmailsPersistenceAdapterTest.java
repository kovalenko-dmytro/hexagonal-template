package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailType;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class GetEmailsPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private GetEmailsPersistenceAdapter getEmailsPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void getEmails_withoutFilter() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .page(1)
            .size(5)
            .sort(new String[]{"sendBy"})
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new Email[0]), Comparator.comparing(Email::getSendBy)));

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_SendBy() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .sendBy("admin")
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_SendByAndSendTo() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .sendBy("admin")
            .sendTo("email1")
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_SendByAndSendToAndEmailType() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .sendBy("admin")
            .sendTo("email1")
            .emailType(EmailType.INVITE)
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_SendByAndSendToAndEmailTypeAndEmailStatus() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .sendBy("admin")
            .sendTo("email1")
            .emailType(EmailType.INVITE)
            .emailStatus(EmailStatus.ERROR)
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_DateFrom() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .dateSendFrom(LocalDate.of(2024, 8, 24))
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_searchBy_DateFromAndDateTo() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .dateSendFrom(LocalDate.of(2024, 8, 24))
            .dateSendTo(LocalDate.of(2024, 8, 28))
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_sortBy_SubjectDesc() {
        emailRepository.saveAll(EmailsTestData.emails());

        final var requestWrapper = GetEmailsWrapper.builder()
            .sort(new String[]{"subject desc"})
            .page(1)
            .size(5)
            .build();

        final var actual = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new Email[0]), Comparator.comparing(Email::getSubject).reversed()));

        emailRepository.deleteAll();
    }
}