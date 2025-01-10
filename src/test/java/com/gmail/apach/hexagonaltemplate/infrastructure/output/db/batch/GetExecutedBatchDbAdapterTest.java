package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class GetExecutedBatchDbAdapterTest extends AbstractIntegrationTest {

    private static final String BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887b";
    private static final String WRONG_BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887c";

    @Autowired
    private GetExecutedBatchDbAdapter getExecutedBatchDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/batch/get_batch_setup.yml")
    void get_success() {
        final var actual = getExecutedBatchDbAdapter.get(BATCH_ID);

        assertNotNull(actual);
        assertEquals(actual.getBatchId(), BATCH_ID);
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/batch/get_batch_setup.yml")
    void get_notFound() {
        assertThrows(ResourceNotFoundException.class, () -> getExecutedBatchDbAdapter.get(WRONG_BATCH_ID));
    }
}