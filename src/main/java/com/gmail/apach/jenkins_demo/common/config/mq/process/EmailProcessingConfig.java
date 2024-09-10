package com.gmail.apach.jenkins_demo.common.config.mq.process;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailProcessingConfig {

    public static final String EMAIL_DIRECT_EXCHANGE = "email-direct-exchange";
    public static final String EMAIL_QUEUE = "email-queue";
    public static final String EMAIL_ROUTING_KEY = "email";


    @Bean
    public DirectExchange orderDirectExchange() {
        return new DirectExchange(EMAIL_DIRECT_EXCHANGE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE);
    }

    @Bean
    public Binding paymentBinding() {
        return BindingBuilder.bind(emailQueue()).to(orderDirectExchange()).with(EMAIL_ROUTING_KEY);
    }
}
