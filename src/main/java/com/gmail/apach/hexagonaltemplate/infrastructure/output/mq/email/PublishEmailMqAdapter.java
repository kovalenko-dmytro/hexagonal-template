package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailStatus;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email.helper.PrepareEmailPayloadHelper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.dto.SendEmailWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PublishEmailMqAdapter implements PublishEmailOutputPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishSendInviteEmail(User user) {
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.SEND_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.SEND_EMAIL_ROUTING_KEY.getKey(),
            PrepareEmailPayloadHelper.prepareEmailData(user));
    }

    @Override
    public void publishCreateEmail(SendEmailWrapper wrapper, EmailStatus emailStatus) {
        final var email = buildEmail(wrapper, emailStatus);
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.CREATE_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.CREATE_EMAIL_ROUTING_KEY.getKey(),
            email);
    }

    private Email buildEmail(SendEmailWrapper wrapper, EmailStatus emailStatus) {
        return Email.builder()
            .sendBy(wrapper.sendBy()).sendTo(wrapper.sendTo()).cc(wrapper.cc()).subject(wrapper.subject())
            .sendTime(LocalDateTime.now()).emailType(wrapper.emailType()).emailStatus(emailStatus)
            .build();
    }
}
