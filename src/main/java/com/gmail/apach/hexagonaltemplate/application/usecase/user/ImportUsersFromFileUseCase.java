package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersFromFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.ImportUsersFromFileOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImportUsersFromFileUseCase implements ImportUsersFromFileInputPort {

    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;
    private final ImportUsersFromFileOutputPort importUsersFromFileOutputPort;

    @Override
    public ExecutedBatch execute(String fileId) {
        final var batchId = UUID.randomUUID().toString();
        final var executedBy = currentPrincipalOutputPort.getPrincipal().getUsername();
        importUsersFromFileOutputPort.execute(batchId, fileId, executedBy);
        return ExecutedBatch.builder().batchId(batchId).build();
    }
}
