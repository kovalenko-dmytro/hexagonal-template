package com.gmail.apach.hexagonaltemplate.application.usecase.batch;

import com.gmail.apach.hexagonaltemplate.application.port.output.batch.GetExecutedBatchOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetExecutedBatchUseCaseTest {

    private static final String BATCH_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @InjectMocks
    private GetExecutedBatchUseCase getExecutedBatchUseCase;
    @Mock
    private GetExecutedBatchOutputPort getExecutedBatchOutputPort;

    @Test
    void get_success() {
        when(getExecutedBatchOutputPort.get(BATCH_ID))
            .thenReturn(ExecutedBatch.builder().batchId(BATCH_ID).build());

        final var actual = getExecutedBatchUseCase.get(BATCH_ID);

        assertNotNull(actual);
        assertEquals(BATCH_ID, actual.getBatchId());
    }

    @Test
    void get_fail() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getExecutedBatchOutputPort).get(BATCH_ID);

        assertThrows(ResourceNotFoundException.class, () -> getExecutedBatchUseCase.get(BATCH_ID));
    }
}