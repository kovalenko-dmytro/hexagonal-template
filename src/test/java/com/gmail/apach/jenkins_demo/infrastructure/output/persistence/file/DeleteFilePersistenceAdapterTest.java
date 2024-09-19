package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteFilePersistenceAdapterTest extends AbstractIntegrationTest {

    private static final String FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512452";

    @Autowired
    private DeleteFilePersistenceAdapter deleteFilePersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/delete_file_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/output/persistence/file/delete_file_expected.yml")
    void deleteFile() {
        deleteFilePersistenceAdapter.deleteFile(FILE_ID);
    }
}