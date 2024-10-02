package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email;

import com.gmail.apach.hexagonaltemplate.application.output.email.CreateEmailPublisher;
import com.gmail.apach.hexagonaltemplate.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.model.EmailStatus;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.dto.SendEmailWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateEmailMqAdapter implements CreateEmailPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishCreateEmail(SendEmailWrapper wrapper, EmailStatus emailStatus) {
        final var email = Email.builder()
            .sendBy(wrapper.sendBy())
            .sendTo(wrapper.sendTo())
            .cc(wrapper.cc())
            .subject(wrapper.subject())
            .sendTime(LocalDateTime.now())
            .emailType(wrapper.emailType())
            .emailStatus(emailStatus)
            .build();

        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.CREATE_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.CREATE_EMAIL_ROUTING_KEY.getKey(),
            email);
    }
}
