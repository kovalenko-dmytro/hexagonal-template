package com.gmail.apach.hexagonaltemplate.application.usecase.batch;

import com.gmail.apach.hexagonaltemplate.application.port.input.batch.GetExecutedBatchInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.batch.GetExecutedBatchOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetExecutedBatchUseCase implements GetExecutedBatchInputPort {

    private final GetExecutedBatchOutputPort getExecutedBatchOutputPort;

    @Override
    public ExecutedBatch get(String batchId) {
        return getExecutedBatchOutputPort.get(batchId);
    }
}
