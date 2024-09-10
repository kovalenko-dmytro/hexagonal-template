package com.gmail.apach.jenkins_demo.common.config.email;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class EmailConfigProperties {

    @Value("${application.default-admin.email}")
    private String adminEmail;
}
