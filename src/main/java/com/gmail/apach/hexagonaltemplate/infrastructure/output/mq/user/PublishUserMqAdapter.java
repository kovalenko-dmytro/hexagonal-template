package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.UserProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.ImportUsersWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PublishUserMqAdapter implements PublishUserOutputPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishImportUsers(List<User> users) {
        rabbitTemplate.convertAndSend(
            UserProcessingConfig.Exchanges.IMPORT_USERS_DIRECT_EXCHANGE.getExchange(),
            UserProcessingConfig.RoutingKeys.IMPORT_USERS_ROUTING_KEY.getKey(),
            new ImportUsersWrapper(users));
    }
}
