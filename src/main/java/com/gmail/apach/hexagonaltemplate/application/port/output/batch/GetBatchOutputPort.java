package com.gmail.apach.hexagonaltemplate.application.port.output.batch;

import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;

public interface GetBatchOutputPort {
    ExecutedBatch get(String batchId);
}
