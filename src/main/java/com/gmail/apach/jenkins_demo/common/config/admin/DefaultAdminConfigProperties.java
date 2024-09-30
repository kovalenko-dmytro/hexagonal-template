package com.gmail.apach.jenkins_demo.common.config.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application.default-admin")
@Data
public class DefaultAdminConfigProperties {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String email;
}
