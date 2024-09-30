package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.jwt")
@Data
public class JWTConfigProperties {

    @NotBlank
    private String secretKey;
    @NotBlank
    private long expiration;
    @NotBlank
    private long refreshExpiration;
}
