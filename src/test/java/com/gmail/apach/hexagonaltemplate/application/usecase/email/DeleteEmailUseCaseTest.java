package com.gmail.apach.hexagonaltemplate.application.usecase.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.DeleteEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteEmailUseCaseTest {

    private static final String EMAIL_ID = "eeed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private DeleteEmailUseCase deleteEmailUseCase;
    @Mock
    private GetEmailOutputPort getEmailOutputPort;
    @Mock
    private DeleteEmailOutputPort deleteEmailOutputPort;

    @Test
    void deleteByEmailId_success() {
        final var email = EmailsTestData.email();
        email.setEmailId(EMAIL_ID);

        when(getEmailOutputPort.getByEmailId(EMAIL_ID)).thenReturn(email);
        doNothing().when(deleteEmailOutputPort).deleteByEmailId(email.getEmailId());

        assertDoesNotThrow(() -> deleteEmailUseCase.deleteByEmailId(email.getEmailId()));
    }

    @Test
    void deleteByEmailId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getEmailOutputPort).getByEmailId(EMAIL_ID);

        assertThrows(ResourceNotFoundException.class, () -> deleteEmailUseCase.deleteByEmailId(EMAIL_ID));
    }
}