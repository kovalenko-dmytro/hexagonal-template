package com.gmail.apach.hexagonaltemplate.application.port.input.batch;


import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;

public interface GetBatchInputPort {
    ExecutedBatch get(String batchId);
}
