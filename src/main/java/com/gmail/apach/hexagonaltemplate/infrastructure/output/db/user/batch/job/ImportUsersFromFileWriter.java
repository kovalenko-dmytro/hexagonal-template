package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobParameterKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ImportUsersFromFileWriter implements Tasklet, StepExecutionListener {

    private final CreateUserOutputPort createUserOutputPort;

    private String batchId;
    private List<User> users;

    @Override
    @SuppressWarnings("unchecked")
    public void beforeStep(@NonNull StepExecution stepExecution) {
        final var executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
        this.users = (List<User>) executionContext.get("users");
        final var jobParameters = stepExecution.getJobExecution().getJobParameters();
        this.batchId = jobParameters.getString(JobParameterKey.BATCH_ID);
        log.info("Step {} for job id: {} has been initialized.", stepExecution.getStepName(), batchId);
    }

    @Override
    public RepeatStatus execute(
        @NonNull StepContribution contribution,
        @NonNull ChunkContext chunkContext
    ) throws Exception {
        createUserOutputPort.create(users);
        log.info("Step {} for job id: {} has been executed.", contribution.getStepExecution().getStepName(), batchId);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        log.info("Step {} for job id: {} has been completed.", stepExecution.getStepName(), batchId);
        return ExitStatus.COMPLETED;
    }
}
