package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ImportUsersInsertStepConfig {

    private static final String STEP_NAME = "INSERT USER RECORDS To DB STEP";

    private final ImportUsersItemReader reader;
    private final ImportUsersItemProcessor processor;
    private final ImportUsersItemWriter writer;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public Step insertStep() {
        final var builder = new StepBuilder(STEP_NAME, jobRepository);
        return builder
            .<List<User>, List<UserEntity>>chunk(10, platformTransactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }
}
