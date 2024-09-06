package com.gmail.apach.jenkins_demo.common.config.mq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Data
public class RabbitMQConfigProperties {

    private String host;
    private String port;
    private String username;
    private String password;
}
