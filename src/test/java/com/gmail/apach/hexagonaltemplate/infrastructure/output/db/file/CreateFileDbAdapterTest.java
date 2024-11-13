package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CreateFileDbAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateFileDbAdapter createFileDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/file/create_file_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/db/file/create_file_expected.yml",
        ignoreCols = {"file_", "created"})
    void createFile() {
        final var expected = StoredFile.builder()
            .storageKey("key68c8-2f28-4b53-ac5a-2db586512441")
            .fileName("test.txt")
            .contentType("text/plain")
            .size(25L)
            .created(LocalDateTime.now())
            .build();

        final var actual = createFileDbAdapter.createFile(expected);

        assertNotNull(actual);
        assertEquals(expected.getFileName(), actual.getFileName());
        assertEquals(expected.getStorageKey(), actual.getStorageKey());
    }
}