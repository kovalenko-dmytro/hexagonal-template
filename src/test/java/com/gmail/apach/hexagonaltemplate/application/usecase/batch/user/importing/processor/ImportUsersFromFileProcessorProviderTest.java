package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ImportUsersFromFileProcessorProviderTest extends AbstractIntegrationTest {

    @Autowired
    private ImportUsersFromFileProcessorProvider provider;

    @Test
    void getProcessor_success() {
        assertDoesNotThrow(() ->
            provider.getProcessor(FileContentType.CSV));
        assertDoesNotThrow(() ->
            provider.getProcessor(FileContentType.EXCEL));
    }

    @Test
    void getProcessor_fail() {
        assertThrows(IllegalArgumentException.class,
            () -> provider.getProcessor("wrong_content_type"));
    }
}