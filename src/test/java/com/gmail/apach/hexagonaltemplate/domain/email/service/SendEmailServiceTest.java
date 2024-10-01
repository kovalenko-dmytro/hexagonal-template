package com.gmail.apach.hexagonaltemplate.domain.email.service;

import com.gmail.apach.hexagonaltemplate.application.output.email.CreateEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.common.config.admin.DefaultAdminConfigProperties;
import com.gmail.apach.hexagonaltemplate.data.CreateUserTestData;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
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

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendEmailServiceTest {

    @InjectMocks
    private SendEmailService sendEmailService;
    @Mock
    private JavaMailSender emailSender;
    @Mock
    private SpringTemplateEngine templateEngine;
    @Mock
    private CreateEmailOutputPort createEmailOutputPort;
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

        assertDoesNotThrow(() -> sendEmailService.sendEmail(emailData));

        verify(createEmailOutputPort, times(1)).createEmail(any(Email.class));
    }

    @Test
    void sendEmail_sendError() {
        final var emailData = CreateUserTestData.emailData();

        final var message = mock(MimeMessage.class);
        when(defaultAdminConfigProperties.getEmail()).thenReturn("admin@gmail.com");
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("payload");
        when(emailSender.createMimeMessage()).thenReturn(message);
        doThrow(new MailSendException("error"))
            .when(emailSender).send(message);

        sendEmailService.sendEmail(emailData);

        verify(createEmailOutputPort, times(1)).createEmail(any(Email.class));
        verify(messageSource, times(1)).getMessage(any(String.class), any(), any(Locale.class));
    }
}