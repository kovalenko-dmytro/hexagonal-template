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
public class EmailProcessingConfig {

    public static final String SEND_EMAIL_QUEUE = "send-email-queue";
    public static final String SAVE_EMAIL_QUEUE = "save-email-queue";

    @RequiredArgsConstructor
    @Getter
    public enum Exchanges {
        SEND_EMAIL_DIRECT_EXCHANGE("send-email-direct-exchange"),
        SAVE_EMAIL_DIRECT_EXCHANGE("save-email-direct-exchange");

        private final String exchange;
    }

    @RequiredArgsConstructor
    @Getter
    public enum RoutingKeys {
        SEND_EMAIL_ROUTING_KEY("send-email"),
        SAVE_EMAIL_ROUTING_KEY("save-email");

        private final String key;
    }

    @Bean
    public DirectExchange sendEmailDirectExchange() {
        return new DirectExchange(Exchanges.SEND_EMAIL_DIRECT_EXCHANGE.getExchange());
    }

    @Bean
    public Queue sendEmailQueue() {
        return new Queue(SEND_EMAIL_QUEUE);
    }

    @Bean
    public Binding sendEmailBinding() {
        return BindingBuilder
            .bind(sendEmailQueue())
            .to(sendEmailDirectExchange())
            .with(RoutingKeys.SEND_EMAIL_ROUTING_KEY.getKey());
    }

    @Bean
    public DirectExchange saveEmailDirectExchange() {
        return new DirectExchange(Exchanges.SAVE_EMAIL_DIRECT_EXCHANGE.getExchange());
    }

    @Bean
    public Queue saveEmailQueue() {
        return new Queue(SAVE_EMAIL_QUEUE);
    }

    @Bean
    public Binding saveEmailBinding() {
        return BindingBuilder
            .bind(saveEmailQueue())
            .to(saveEmailDirectExchange())
            .with(RoutingKeys.SAVE_EMAIL_ROUTING_KEY.getKey());
    }
}
