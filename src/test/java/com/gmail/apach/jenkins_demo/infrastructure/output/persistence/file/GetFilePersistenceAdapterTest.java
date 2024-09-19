package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class GetFilePersistenceAdapterTest extends AbstractIntegrationTest {

    private static final String FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512452";
    private static final String ABSENT_FILE_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512488";

    @Autowired
    private GetFilePersistenceAdapter getFilePersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_file_setup.yml")
    void getByFileId_success() {
        final var actual = getFilePersistenceAdapter.getByFileId(FILE_ID);

        assertNotNull(actual);
        assertEquals(FILE_ID, actual.getFileId());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_file_setup.yml")
    void getByFileId_notFound() {
        assertThrows(ResourceNotFoundException.class,
            () -> getFilePersistenceAdapter.getByFileId(ABSENT_FILE_ID));
    }
}