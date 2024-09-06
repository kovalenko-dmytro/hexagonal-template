package com.gmail.apach.jenkins_demo.domain.email.service;

import com.gmail.apach.jenkins_demo.application.output.email.CreateEmailOutputPort;
import com.gmail.apach.jenkins_demo.common.config.email.EmailConfigProperties;
import com.gmail.apach.jenkins_demo.data.CreateUserTestData;
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
    private EmailConfigProperties emailConfigProperties;


    @Test
    void sendEmail_success() {
        final var emailData = CreateUserTestData.emailData();

        final var message = mock(MimeMessage.class);
        when(emailConfigProperties.getAdminEmail()).thenReturn("admin@gmail.com");
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("payload");
        when(emailSender.createMimeMessage()).thenReturn(message);

        assertDoesNotThrow(() -> sendEmailService.sendEmail(emailData));
    }

    @Test
    void sendEmail_sendError() {
        final var emailData = CreateUserTestData.emailData();

        final var message = mock(MimeMessage.class);
        when(emailConfigProperties.getAdminEmail()).thenReturn("admin@gmail.com");
        when(templateEngine.process(any(String.class), any(IContext.class))).thenReturn("payload");
        when(emailSender.createMimeMessage()).thenReturn(message);
        doThrow(new MailSendException("error"))
            .when(emailSender).send(message);

        sendEmailService.sendEmail(emailData);
    }
}