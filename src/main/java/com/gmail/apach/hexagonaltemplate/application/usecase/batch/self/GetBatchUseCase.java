package com.gmail.apach.hexagonaltemplate.application.usecase.batch.self;

import com.gmail.apach.hexagonaltemplate.application.port.input.batch.GetBatchInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.batch.GetBatchOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBatchUseCase implements GetBatchInputPort {

    private final GetBatchOutputPort getBatchOutputPort;

    @Override
    public ExecutedBatch get(String batchId) {
        return getBatchOutputPort.get(batchId);
    }
}
