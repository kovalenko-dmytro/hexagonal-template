package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor;

import com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor.impl.ImportUsersFromExcelProcessor;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromExcelProcessorTest {

    private static final String FILE_PATH = "src/test/resources/file/insert_users.xlsx";

    @InjectMocks
    private ImportUsersFromExcelProcessor importUsersFromExcelProcessor;

    @Test
    void process_success() throws IOException {
        final var storedFile = StoredFile.builder()
            .fileName("insert_users.xlsx")
            .storedResource(StoredResource.builder()
                .payload(Files.readAllBytes(Paths.get(FILE_PATH)))
                .build())
            .build();

        final var actual = importUsersFromExcelProcessor.process(storedFile);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(20, actual.size());
    }
}