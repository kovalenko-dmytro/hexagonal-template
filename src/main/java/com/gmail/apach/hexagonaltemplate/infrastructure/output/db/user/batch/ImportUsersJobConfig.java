package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ImportUsersJobConfig {

    private static final String JOB_NAME = "USERS IMPORT JOB";

    private final Step insertStep;
    private final JobRepository jobRepository;
    private final JobCompletionNotificationListener completionNotificationListener;

    @Bean
    public Job importUsersJob() {
        final var builder = new JobBuilder(JOB_NAME, jobRepository);
        return builder
            .start(insertStep)
            .listener(completionNotificationListener)
            .build();
    }
}
