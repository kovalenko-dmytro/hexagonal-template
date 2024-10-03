package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.jwt;

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
