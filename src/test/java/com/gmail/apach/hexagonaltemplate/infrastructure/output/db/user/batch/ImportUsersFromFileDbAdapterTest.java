package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobRegistry;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromFileDbAdapterTest {

    private static final String BATCH_ID = "batch-id";
    private static final String FILE_ID = "file-id";
    private static final String EXECUTED_BY = "admin";

    @InjectMocks
    private ImportUsersFromFileDbAdapter importUsersFromFileDbAdapter;
    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job job;
    @Mock
    private JobExecution jobExecution;

    @Test
    void execute_success()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
        JobParametersInvalidException, JobRestartException {

        when(applicationContext.getBean(JobRegistry.IMPORT_USERS_FROM_FILE_JOB.getJobBean(), Job.class))
            .thenReturn(job);
        when(jobLauncher.run(any(Job.class), any(JobParameters.class)))
            .thenReturn(jobExecution);
        when(jobExecution.getStatus())
            .thenReturn(BatchStatus.COMPLETED);

        assertDoesNotThrow(() ->
            importUsersFromFileDbAdapter.execute(BATCH_ID, FILE_ID, EXECUTED_BY));
    }

    @Test
    void execute_fail()
        throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException,
        JobParametersInvalidException, JobRestartException {

        when(applicationContext.getBean(JobRegistry.IMPORT_USERS_FROM_FILE_JOB.getJobBean(), Job.class))
            .thenReturn(job);
        doThrow(new JobExecutionAlreadyRunningException("AlreadyRunning"))
            .when(jobLauncher)
            .run(any(Job.class), any(JobParameters.class));

        assertThrows(ApplicationServerException.class,
            () -> importUsersFromFileDbAdapter.execute(BATCH_ID, FILE_ID, EXECUTED_BY));
    }
}