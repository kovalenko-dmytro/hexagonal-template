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
public class UserProcessingConfig {
    public static final String IMPORT_USERS_QUEUE = "import-users-queue";

    @RequiredArgsConstructor
    @Getter
    public enum Exchanges {
        IMPORT_USERS_DIRECT_EXCHANGE("import-users-direct-exchange");

        private final String exchange;
    }

    @RequiredArgsConstructor
    @Getter
    public enum RoutingKeys {
        IMPORT_USERS_ROUTING_KEY("import-users");

        private final String key;
    }

    @Bean
    public DirectExchange importUsersDirectExchange() {
        return new DirectExchange(UserProcessingConfig.Exchanges.IMPORT_USERS_DIRECT_EXCHANGE.getExchange());
    }

    @Bean
    public Queue importUsersQueue() {
        return new Queue(IMPORT_USERS_QUEUE);
    }

    @Bean
    public Binding importUsersBinding() {
        return BindingBuilder
            .bind(importUsersQueue())
            .to(importUsersDirectExchange())
            .with(UserProcessingConfig.RoutingKeys.IMPORT_USERS_ROUTING_KEY.getKey());
    }
}
