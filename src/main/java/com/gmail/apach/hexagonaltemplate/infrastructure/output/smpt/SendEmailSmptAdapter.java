package com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.email.SendEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.admin.DefaultAdminConfigProperties;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendEmailSmptAdapter implements SendEmailOutputPort {

    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;
    private final PublishEmailOutputPort publishEmailOutputPort;
    private final MessageSource messageSource;
    private final DefaultAdminConfigProperties defaultAdminConfigProperties;

    @Override
    @RabbitListener(queues = EmailProcessingConfig.SEND_EMAIL_QUEUE)
    public void sendEmail(SendEmailWrapper wrapper) {
        final var payload = obtainPayload(wrapper);
        final var emailStatus = send(payload, wrapper);
        publishEmailOutputPort.publishSaveEmail(toEmail(wrapper, emailStatus));
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

    private EmailStatus send(String payload, SendEmailWrapper wrapper) {
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
            log.warn(messageSource.getMessage(
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

    private Email toEmail(SendEmailWrapper wrapper, EmailStatus emailStatus) {
        return Email.builder()
            .sendBy(wrapper.sendBy()).sendTo(wrapper.sendTo()).cc(wrapper.cc()).subject(wrapper.subject())
            .sendTime(LocalDateTime.now()).emailType(wrapper.emailType()).emailStatus(emailStatus)
            .build();
    }
}
