package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.StorageServiceProviderType;
import lombok.NonNull;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@StepScope
@Setter
public class ImportUsersFromFileIReader implements Tasklet, StepExecutionListener {

    private final GetFileOutputPort getFileOutputPort;
    @Qualifier(StorageServiceProviderType.S3)
    private final ObjectStorageServiceOutputPort objectStorageServiceOutputPort;

    @Value("#{jobParameters['fileId']}")
    private String fileId;
    @Value("#{jobParameters['batchId']}")
    private String batchId;

    private StoredFile storedFile;

    @Override
    public void beforeStep(@NonNull StepExecution stepExecution) {
        StepExecutionListener.super.beforeStep(stepExecution);
        log.info("Step {} for job id: {} has been initialized.", stepExecution.getStepName(), batchId);
    }

    @Override
    public RepeatStatus execute(
        @NonNull StepContribution contribution,
        @NonNull ChunkContext chunkContext
    ) throws Exception {
        final var storedFile = getFileOutputPort.getByFileId(fileId);
        final var resource = objectStorageServiceOutputPort.get(storedFile.getStoredResource().getStorageKey());
        storedFile.setStoredResource(resource);
        this.storedFile = storedFile;
        log.info("Step {} for job id: {} has been executed.", contribution.getStepExecution().getStepName(), batchId);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(@NonNull StepExecution stepExecution) {
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put("storedFile", this.storedFile);
        log.info("Step {} for job id: {} has been completed.", stepExecution.getStepName(), batchId);
        return ExitStatus.COMPLETED;
    }
}
