package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.job;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
@Setter
public class ImportUsersWriter implements Tasklet, StepExecutionListener {

    private final CreateUserOutputPort createUserOutputPort;

    @Value("#{jobParameters['jobId']}")
    private String jobId;

    private List<User> users;

    @Override
    @SuppressWarnings("unchecked")
    public void beforeStep(@NonNull StepExecution stepExecution) {
        final var executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
        this.users = (List<User>) executionContext.get("users");
        log.info("Step {} for job id: {} has been initialized.", stepExecution.getStepName(), jobId);
    }

    @Override
    public RepeatStatus execute(
        @NonNull StepContribution contribution,
        @NonNull ChunkContext chunkContext
    ) throws Exception {
        createUserOutputPort.create(users);
        log.info("Step {} for job id: {} has been executed.", contribution.getStepExecution().getStepName(), jobId);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        log.info("Step {} for job id: {} has been completed.", stepExecution.getStepName(), jobId);
        return ExitStatus.COMPLETED;
    }
}
