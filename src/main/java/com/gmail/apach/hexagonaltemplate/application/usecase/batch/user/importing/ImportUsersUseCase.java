package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.batch.common.JobParameterKey;
import com.gmail.apach.hexagonaltemplate.application.usecase.batch.common.JobRegistry;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportUsersUseCase implements ImportUsersInputPort {

    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;
    private final MessageSource messageSource;

    @Async
    @Override
    public void execute(String jobId, String fileId, String username) {
        log.info("Job name: {} with id: {} is initialized.", JobRegistry.IMPORT_USERS_JOB.getJobName(), jobId);
        final var job = applicationContext.getBean(JobRegistry.IMPORT_USERS_JOB.getJobBean(), Job.class);

        final var jobParameters = new JobParametersBuilder()
            .addString(JobParameterKey.JOB_ID, jobId)
            .addString(JobParameterKey.FILE_ID, fileId)
            .addString(JobParameterKey.PRINCIPAL_USERNAME, username)
            .toJobParameters();

        try {
            final var execution = jobLauncher.run(job, jobParameters);
            log.info("Job name: {} with id: {} is finished with status: {}.",
                JobRegistry.IMPORT_USERS_JOB.getJobName(), jobId, execution.getStatus().name());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            final var errorArgs = new Object[]{jobId, job.getName(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(Error.JOB_FAILED.getKey(), errorArgs, LocaleContextHolder.getLocale()));
        }
    }
}
