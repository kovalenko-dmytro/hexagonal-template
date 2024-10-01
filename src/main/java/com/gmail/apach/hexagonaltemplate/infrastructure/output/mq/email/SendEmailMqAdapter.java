package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email;

import com.gmail.apach.hexagonaltemplate.application.output.email.SendEmailPublisher;
import com.gmail.apach.hexagonaltemplate.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.SendEmailWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailMqAdapter implements SendEmailPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(SendEmailWrapper wrapper) {
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.EMAIL_DIRECT_EXCHANGE,
            EmailProcessingConfig.EMAIL_ROUTING_KEY,
            wrapper);
    }
}
