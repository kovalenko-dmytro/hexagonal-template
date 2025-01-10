package com.gmail.apach.hexagonaltemplate.application.port.output.batch;

import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;

public interface GetExecutedBatchOutputPort {
    ExecutedBatch get(String batchId);
}
