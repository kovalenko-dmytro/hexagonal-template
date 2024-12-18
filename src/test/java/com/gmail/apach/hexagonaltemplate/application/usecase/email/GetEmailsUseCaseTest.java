package com.gmail.apach.hexagonaltemplate.application.usecase.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.wrapper.GetEmailsFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetEmailsUseCaseTest {

    @InjectMocks
    private GetEmailsUseCase getEmailsUseCase;
    @Mock
    private GetEmailsOutputPort getEmailsOutputPort;

    @Test
    void getEmails_success() {
        final var emails = new PageImpl<>(List.of(EmailsTestData.email(), EmailsTestData.anotherEmail()));

        when(getEmailsOutputPort.get(any(GetEmailsFilterWrapper.class))).thenReturn(emails);

        final var actual =
            getEmailsUseCase.get(null, null, null, null, null,
                null, 1, 1, new String[]{"sendBy"});

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
    }
}