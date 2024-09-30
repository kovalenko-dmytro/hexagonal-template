package com.gmail.apach.jenkins_demo.domain.email.service;

import com.gmail.apach.jenkins_demo.application.input.email.SendEmailInputPort;
import com.gmail.apach.jenkins_demo.application.output.email.CreateEmailOutputPort;
import com.gmail.apach.jenkins_demo.common.config.admin.DefaultAdminConfigProperties;
import com.gmail.apach.jenkins_demo.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.jenkins_demo.common.constant.CommonConstant;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import com.gmail.apach.jenkins_demo.domain.email.model.Email;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailStatus;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendEmailService implements SendEmailInputPort {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final CreateEmailOutputPort createEmailOutputPort;
    private final MessageSource messageSource;
    private final DefaultAdminConfigProperties defaultAdminConfigProperties;

    @Override
    @RabbitListener(queues = EmailProcessingConfig.EMAIL_QUEUE)
    public void sendEmail(SendEmailWrapper wrapper) {
        final var payload = obtainPayload(wrapper);

        final var emailStatus = sendPreparedEmail(payload, wrapper);

        final var email = Email.builder()
            .sendBy(wrapper.sendBy())
            .sendTo(wrapper.sendTo())
            .cc(wrapper.cc())
            .subject(wrapper.subject())
            .sendTime(LocalDateTime.now())
            .emailType(wrapper.emailType())
            .emailStatus(emailStatus)
            .build();

        createEmailOutputPort.createEmail(email);
    }

    private String obtainPayload(SendEmailWrapper wrapper) {
        final var locale = LocaleContextHolder.getLocale();
        final var templateContext = new Context(locale);
        templateContext.setVariables(wrapper.properties());

        final var emailType = wrapper.emailType();
        if (Objects.isNull(emailType.getTemplate())) {
            throw new ResourceNotFoundException(
                messageSource.getMessage(
                    Error.EMAIL_TEMPLATE_NOT_FOUND.getKey(), new Object[]{emailType}, locale));
        }
        return templateEngine.process(emailType.getTemplate(), templateContext);
    }

    private EmailStatus sendPreparedEmail(String payload, SendEmailWrapper wrapper) {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, CommonConstant.DEFAULT_CHARSET.getValue());
            helper.setFrom(defaultAdminConfigProperties.getEmail());
            helper.setTo(wrapper.sendTo());
            if (CollectionUtils.isNotEmpty(wrapper.cc())) {
                helper.setCc(wrapper.cc().toArray(String[]::new));
            }
            helper.setSubject(wrapper.subject());
            helper.setText(payload, true);
            if (Objects.nonNull(wrapper.attachments())) {
                addAttachments(helper, wrapper.attachments());
            }
            emailSender.send(message);
            return EmailStatus.SEND;
        } catch (MailSendException | MessagingException | IOException e) {
            Object[] params = new Object[]{wrapper.sendTo(), e.getMessage()};
            log.error(messageSource.getMessage(
                Error.EMAIL_SEND_ERROR.getKey(), params, LocaleContextHolder.getLocale()));
            return EmailStatus.ERROR;
        }
    }

    private void addAttachments(MimeMessageHelper helper, MultipartFile[] attachments)
        throws MessagingException, IOException {
        for (MultipartFile attachment : attachments) {
            if (Objects.nonNull(attachment)) {
                helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()), attachment);
            }
        }
    }
}
