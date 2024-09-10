package com.gmail.apach.jenkins_demo.infrastructure.output.mq.email;

import com.gmail.apach.jenkins_demo.application.output.email.SendEmailPublisher;
import com.gmail.apach.jenkins_demo.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;
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
