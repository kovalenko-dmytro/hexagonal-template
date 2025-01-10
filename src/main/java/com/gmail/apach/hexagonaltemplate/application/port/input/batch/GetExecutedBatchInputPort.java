package com.gmail.apach.hexagonaltemplate.application.port.input.batch;


import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;

public interface GetExecutedBatchInputPort {
    ExecutedBatch get(String batchId);
}
