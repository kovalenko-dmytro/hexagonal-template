package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.ImportUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.UserProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.JobRegistry;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.ImportUsersWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImportUsersDbAdapter implements ImportUsersOutputPort {

    private final ApplicationContext applicationContext;
    private final JobLauncher jobLauncher;
    private final MessageSource messageSource;

    @CacheEvict(
        value = UserCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @RabbitListener(queues = UserProcessingConfig.IMPORT_USERS_QUEUE)
    @Override
    public void execute(ImportUsersWrapper wrapper) {
        final var job = applicationContext.getBean(JobRegistry.IMPORT_USERS_JOB.getJobBean(), Job.class);
        final var jobId = String.valueOf(System.currentTimeMillis());

        final var jobParameters = new JobParametersBuilder()
            .addString("JobID", jobId)
            .toJobParameters();

        try {
            final var jobExecution = jobLauncher.run(job, jobParameters);
            ExecutionContext context= jobExecution.getExecutionContext();
            context.put("USERS", wrapper.users());
            log.info("{} execution completed with status: {}", job.getName(), jobExecution.getStatus());
        } catch (JobExecutionAlreadyRunningException | JobRestartException |
                 JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            final var errorArgs = new Object[]{jobId, job.getName(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(Error.JOB_FAILED.getKey(), errorArgs, LocaleContextHolder.getLocale()));
        }
    }
}
