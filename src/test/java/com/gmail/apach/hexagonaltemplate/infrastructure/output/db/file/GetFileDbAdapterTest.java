package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class GetFileDbAdapterTest extends AbstractIntegrationTest {

    private static final String FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512452";
    private static final String ABSENT_FILE_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512488";

    @Autowired
    private GetFileDbAdapter getFileDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_file_setup.yml")
    void getByFileId_success() {
        final var actual = getFileDbAdapter.getByFileId(FILE_ID);

        assertNotNull(actual);
        assertEquals(FILE_ID, actual.getFileId());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_file_setup.yml")
    void getByFileId_notFound() {
        assertThrows(ResourceNotFoundException.class,
            () -> getFileDbAdapter.getByFileId(ABSENT_FILE_ID));
    }
}