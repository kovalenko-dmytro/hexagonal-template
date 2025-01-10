package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ImportUsersFromFileJob {
    private static final String IMPORT_USERS_READ_STEP_NAME = "IMPORT-USERS-JOB-READ-STEP";
    protected static final String IMPORT_USERS_PROCESS_STEP_NAME = "IMPORT-USERS-JOB-PROCESS-STEP";
    protected static final String IMPORT_USERS_WRITE_STEP_NAME = "IMPORT-USERS-JOB-WRITE-STEP";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ImportUsersFromFileIReader reader;
    private final ImportUsersFromFileProcessor processor;
    private final ImportUsersFromFileWriter writer;

    @Bean
    protected Step readStep() {
        return new StepBuilder(IMPORT_USERS_READ_STEP_NAME, jobRepository)
            .tasklet(reader, transactionManager)
            .build();
    }

    @Bean
    protected Step processStep() {
        return new StepBuilder(IMPORT_USERS_PROCESS_STEP_NAME, jobRepository)
            .tasklet(processor, transactionManager)
            .build();
    }

    @Bean
    protected Step writeStep() {
        return new StepBuilder(IMPORT_USERS_WRITE_STEP_NAME, jobRepository)
            .tasklet(writer, transactionManager)
            .build();
    }

    @Bean
    public Job importUsersJob() {
        final var builder = new JobBuilder(JobRegistry.IMPORT_USERS_JOB.getJobName(), jobRepository);
        return builder
            .start(readStep())
            .next(processStep())
            .next(writeStep())
            .build();
    }
}
