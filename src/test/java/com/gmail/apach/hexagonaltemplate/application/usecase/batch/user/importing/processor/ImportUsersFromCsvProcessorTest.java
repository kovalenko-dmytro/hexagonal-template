package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor;

import com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor.impl.ImportUsersFromCsvProcessor;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromCsvProcessorTest {

    @InjectMocks
    private ImportUsersFromCsvProcessor importUsersFromCsvProcessor;

    @Test
    void process_unsupportedOperation() {
        final var storedFile = mock(StoredFile.class);
        assertThrows(UnsupportedOperationException.class, () -> importUsersFromCsvProcessor.process(storedFile));
    }
}