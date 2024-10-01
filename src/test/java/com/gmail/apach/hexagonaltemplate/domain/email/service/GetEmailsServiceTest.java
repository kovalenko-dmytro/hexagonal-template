package com.gmail.apach.hexagonaltemplate.domain.email.service;

import com.gmail.apach.hexagonaltemplate.application.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEmailsServiceTest {

    @InjectMocks
    private GetEmailsService getEmailsService;
    @Mock
    private GetEmailsOutputPort getEmailsOutputPort;

    @Test
    void getEmails_success() {
        final var emails = new PageImpl<>(List.of(EmailsTestData.email(), EmailsTestData.anotherEmail()));
        final var wrapper = GetEmailsWrapper.builder().page(1).size(1).build();

        when(getEmailsOutputPort.getEmails(wrapper)).thenReturn(emails);

        final var actual = getEmailsService.getEmails(wrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
    }
}