package com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.data.CreateUserTestData;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.admin.DefaultAdminConfigProperties;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendEmailSmptAdapterTest {

    @InjectMocks
    private SendEmailSmptAdapter sendEmailSmptAdapter;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private SpringTemplateEngine templateEngine;
    @Mock
    private PublishEmailOutputPort publishEmailOutputPort;
    @Mock
    private MessageSource messageSource;
    @Mock
    private DefaultAdminConfigProperties defaultAdminConfigProperties;

    @Test
    void sendEmail_success() {
        final var emailData = CreateUserTestData.emailData();

        final var message = mock(MimeMessage.class);
        when(defaultAdminConfigProperties.getEmail()).thenReturn("admin@gmail.com");
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("payload");
        when(emailSender.createMimeMessage()).thenReturn(message);

        assertDoesNotThrow(() -> sendEmailSmptAdapter.sendEmail(emailData));

        verify(publishEmailOutputPort, times(1)).publishSaveEmail(any(Email.class));
    }

    @Test
    void sendEmail_sendError() {
        final var emailData = CreateUserTestData.emailData();

        final var message = mock(MimeMessage.class);
        when(defaultAdminConfigProperties.getEmail()).thenReturn("admin@gmail.com");
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("payload");
        when(emailSender.createMimeMessage()).thenReturn(message);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("Unable to send email");
        doThrow(new MailSendException("error")).when(emailSender).send(message);

        sendEmailSmptAdapter.sendEmail(emailData);

        verify(publishEmailOutputPort, times(1)).publishSaveEmail(any(Email.class));
    }
}