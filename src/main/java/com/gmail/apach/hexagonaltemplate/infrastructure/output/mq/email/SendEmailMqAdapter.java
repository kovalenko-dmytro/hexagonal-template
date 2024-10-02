package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email;

import com.gmail.apach.hexagonaltemplate.application.output.email.SendEmailPublisher;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.email.helper.PrepareEmailPayloadHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendEmailMqAdapter implements SendEmailPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishInviteEmail(User user) {
        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.Exchanges.SEND_EMAIL_DIRECT_EXCHANGE.getExchange(),
            EmailProcessingConfig.RoutingKeys.SEND_EMAIL_ROUTING_KEY.getKey(),
            PrepareEmailPayloadHelper.prepareEmailData(user));
    }
}
