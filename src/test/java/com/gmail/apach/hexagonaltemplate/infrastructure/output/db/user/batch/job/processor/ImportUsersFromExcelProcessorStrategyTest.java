package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch.JobParameterKey;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.impl.ImportUsersFromExcelProcessorStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.JobParametersBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromExcelProcessorStrategyTest {

    private static final String FILE_PATH = "src/test/resources/file/insert_users.xlsx";

    @InjectMocks
    private ImportUsersFromExcelProcessorStrategy importUsersFromExcelProcessorStrategy;

    @Test
    void process_success() throws IOException {
        final var storedFile = StoredFile.builder()
            .fileName("insert_users.xlsx")
            .storedResource(StoredResource.builder()
                .payload(Files.readAllBytes(Paths.get(FILE_PATH)))
                .build())
            .build();

        final var jobParameters = new JobParametersBuilder()
            .addString(JobParameterKey.BATCH_ID, "5a8d68c8-2f28-4b53-ac5a-2db586512440")
            .addString(JobParameterKey.FILE_ID, "6y8d68c8-2f28-4b53-ac5a-2db586512445")
            .addString(JobParameterKey.EXECUTED_BY, "admin")
            .toJobParameters();

        final var actual = importUsersFromExcelProcessorStrategy.process(storedFile, jobParameters);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(20, actual.size());
    }
}