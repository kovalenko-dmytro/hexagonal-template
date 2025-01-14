package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.impl.ImportUsersFromCsvProcessorStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParameters;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromCsvProcessorStrategyTest {

    @InjectMocks
    private ImportUsersFromCsvProcessorStrategy importUsersFromCsvProcessorStrategy;

    @Test
    void process_unsupportedOperation() {
        final var storedFile = mock(StoredFile.class);
        final var jobParameters = mock(JobParameters.class);
        assertThrows(UnsupportedOperationException.class,
            () -> importUsersFromCsvProcessorStrategy.process(storedFile, jobParameters));
    }
}