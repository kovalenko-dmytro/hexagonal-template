package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.ImportUsersFromFileProcessorStrategyProvider;
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
public class ImportUsersFromFileProcessor implements Tasklet, StepExecutionListener {

    private final ImportUsersFromFileProcessorStrategyProvider processorStrategyProvider;

    @Value("#{jobParameters['batchId']}")
    private String batchId;

    private StoredFile storedFile;
    private List<User> users;

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        final var executionContext = stepExecution
            .getJobExecution()
            .getExecutionContext();
        this.storedFile = (StoredFile) executionContext.get("storedFile");
        log.info("Step {} for job id: {} has been initialized.", stepExecution.getStepName(), batchId);
    }

    @Override
    public RepeatStatus execute(
        @NonNull StepContribution contribution,
        @NonNull ChunkContext chunkContext
    ) throws Exception {
        this.users = processorStrategyProvider.getProcessor(storedFile.getContentType()).process(storedFile);
        log.info("Step {} for job id: {} has been executed.", contribution.getStepExecution().getStepName(), batchId);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put("users", this.users);
        log.info("Step {} for job id: {} has been completed.", stepExecution.getStepName(), batchId);
        return ExitStatus.COMPLETED;
    }
}
