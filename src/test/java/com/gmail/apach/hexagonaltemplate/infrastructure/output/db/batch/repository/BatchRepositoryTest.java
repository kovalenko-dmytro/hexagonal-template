package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BatchRepositoryTest extends AbstractIntegrationTest {

    private static final String BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887b";
    private static final String WRONG_BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887c";

    @Autowired
    private BatchRepository batchRepository;

    @Test
    @DataSet("datasets/infrastructure/output/db/batch/get_batch_setup.yml")
    void get_success() {
        final var actual = batchRepository.get(BATCH_ID);

        assertTrue(actual.isPresent());
        final var batchView = actual.get();
        assertEquals(batchView.getBatchId(), BATCH_ID);
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/batch/get_batch_setup.yml")
    void get_notExist() {
        final var actual = batchRepository.get(WRONG_BATCH_ID);

        assertTrue(actual.isEmpty());
    }
}