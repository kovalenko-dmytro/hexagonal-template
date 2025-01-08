package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.async;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.ApplicationProfile;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;

@Profile(ApplicationProfile.NOT_TEST)
@Configuration
@EnableAsync
public class AsyncConfig {
}
