package com.gmail.apach.hexagonaltemplate.infrastructure.output.mq.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.PublishFileOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.FileProcessingConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishFileMqAdapter implements PublishFileOutputPort {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishDeleteFile(String storageKey) {
        rabbitTemplate.convertAndSend(
            FileProcessingConfig.Exchanges.DELETE_FILE_DIRECT_EXCHANGE.getExchange(),
            FileProcessingConfig.RoutingKeys.DELETE_FILE_ROUTING_KEY.getKey(),
            storageKey);
    }
}
