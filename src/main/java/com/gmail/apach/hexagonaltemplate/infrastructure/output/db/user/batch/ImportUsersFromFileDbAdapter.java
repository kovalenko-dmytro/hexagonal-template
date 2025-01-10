package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.ImportUsersFromFileOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobParameterKey;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobRegistry;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportUsersFromFileDbAdapter implements ImportUsersFromFileOutputPort {

    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;
    private final MessageSource messageSource;

    @Async
    @Override
    public void execute(String batchId, String fileId, String executedBy) {
        final var job = applicationContext.getBean(JobRegistry.IMPORT_USERS_FROM_FILE_JOB.getJobBean(), Job.class);

        log.info("Job: {} for batch with id: {} is initialized.",
            JobRegistry.IMPORT_USERS_FROM_FILE_JOB.getJobName(), batchId);

        final var jobParameters = new JobParametersBuilder()
            .addString(JobParameterKey.BATCH_ID, batchId)
            .addString(JobParameterKey.FILE_ID, fileId)
            .addString(JobParameterKey.EXECUTED_BY, executedBy)
            .toJobParameters();

        try {
            final var execution = jobLauncher.run(job, jobParameters);
            log.info("Job: {} for batch with id: {} is finished with status: {}.",
                JobRegistry.IMPORT_USERS_FROM_FILE_JOB.getJobName(), batchId, execution.getStatus().name());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            final var errorArgs = new Object[]{batchId, job.getName(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(Error.JOB_FAILED.getKey(), errorArgs, LocaleContextHolder.getLocale()));
        }
    }
}
