package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishEmailMqAdapter implements PublishEmailOutputPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishSend(SendEmailWrapper wrapper) {
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.SEND_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.SEND_EMAIL_ROUTING_KEY.getKey(),
            wrapper);
    }

    @Override
    public void publishSave(Email email) {
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.SAVE_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.SAVE_EMAIL_ROUTING_KEY.getKey(),
            email);
    }
}
