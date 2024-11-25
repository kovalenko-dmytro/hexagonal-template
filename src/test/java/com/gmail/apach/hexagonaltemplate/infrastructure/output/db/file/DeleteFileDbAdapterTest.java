package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteFileDbAdapterTest extends AbstractIntegrationTest {

    private static final String FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512452";

    @Autowired
    private DeleteFileDbAdapter deleteFileDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/file/delete_file_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/output/db/file/delete_file_expected.yml")
    void deleteFile() {
        deleteFileDbAdapter.deleteByFileId(FILE_ID);
    }
}