package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobParameterKey;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.ImportUsersFromFileProcessorStrategyProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
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
public class ImportUsersFromFileProcessor implements Tasklet, StepExecutionListener {

    private final ImportUsersFromFileProcessorStrategyProvider processorStrategyProvider;

    private JobParameters jobParameters;
    private StoredFile storedFile;
    private List<User> users;

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        final var executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
        this.storedFile = (StoredFile) executionContext.get("storedFile");
        this.jobParameters = stepExecution.getJobExecution().getJobParameters();

        log.info("Step {} for job id: {} has been initialized.",
            stepExecution.getStepName(),
            jobParameters.getString(JobParameterKey.BATCH_ID));
    }

    @Override
    public RepeatStatus execute(
        @NonNull StepContribution contribution,
        @NonNull ChunkContext chunkContext
    ) throws Exception {
        this.users = processorStrategyProvider
            .getProcessor(storedFile.getContentType()).process(storedFile, jobParameters);

        log.info("Step {} for job id: {} has been executed.",
            contribution.getStepExecution().getStepName(),
            jobParameters.getString(JobParameterKey.BATCH_ID));

        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put("users", this.users);

        log.info("Step {} for job id: {} has been completed.",
            stepExecution.getStepName(),
            jobParameters.getString(JobParameterKey.BATCH_ID));

        return ExitStatus.COMPLETED;
    }
}
