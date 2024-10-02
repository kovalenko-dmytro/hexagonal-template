package com.gmail.apach.hexagonaltemplate.common.config.mq.process;

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
    public static final String CREATE_EMAIL_QUEUE = "create-email-queue";

    @RequiredArgsConstructor
    @Getter
    public enum Exchanges {
        SEND_EMAIL_DIRECT_EXCHANGE("send-email-direct-exchange"),
        CREATE_EMAIL_DIRECT_EXCHANGE("create-email-direct-exchange");

        private final String exchange;
    }

    @RequiredArgsConstructor
    @Getter
    public enum RoutingKeys {
        SEND_EMAIL_ROUTING_KEY("send-email"),
        CREATE_EMAIL_ROUTING_KEY("create-email");

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
    public DirectExchange createEmailDirectExchange() {
        return new DirectExchange(Exchanges.CREATE_EMAIL_DIRECT_EXCHANGE.getExchange());
    }

    @Bean
    public Queue createEmailQueue() {
        return new Queue(CREATE_EMAIL_QUEUE);
    }

    @Bean
    public Binding createEmailBinding() {
        return BindingBuilder
            .bind(createEmailQueue())
            .to(createEmailDirectExchange())
            .with(RoutingKeys.CREATE_EMAIL_ROUTING_KEY.getKey());
    }
}
