package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileProcessingConfig {

    public static final String DELETE_FILE_QUEUE = "delete-file-queue";

    @RequiredArgsConstructor
    @Getter
    public enum Exchanges {
        DELETE_FILE_DIRECT_EXCHANGE("delete-file-direct-exchange");

        private final String exchange;
    }

    @RequiredArgsConstructor
    @Getter
    public enum RoutingKeys {
        DELETE_FILE_ROUTING_KEY("delete-file");

        private final String key;
    }

    @Bean
    public DirectExchange deleteFileDirectExchange() {
        return new DirectExchange(Exchanges.DELETE_FILE_DIRECT_EXCHANGE.getExchange());
    }

    @Bean
    public Queue deleteFileQueue() {
        return new Queue(DELETE_FILE_QUEUE);
    }

    @Bean
    public Binding deleteFileBinding() {
        return BindingBuilder
            .bind(deleteFileQueue())
            .to(deleteFileDirectExchange())
            .with(RoutingKeys.DELETE_FILE_ROUTING_KEY.getKey());
    }
}
