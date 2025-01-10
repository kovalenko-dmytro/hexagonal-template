package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;

public interface ImportUsersFromFileInputPort {
    ExecutedBatch execute(String fileId);
}
