package com.gmail.apach.hexagonaltemplate.application.usecase.user.importing;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.importing.executor.ImportUsersExecutorProvider;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.StorageServiceProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImportUsersUseCase implements ImportUsersInputPort {

    private final GetFileOutputPort getFileOutputPort;
    @Qualifier(StorageServiceProviderType.S3)
    private final ObjectStorageServiceOutputPort objectStorageServiceOutputPort;
    private final ImportUsersExecutorProvider importUsersExecutorProvider;

    @Override
    public void execute(String fileId) {
        final var storedFile = getFileOutputPort.getByFileId(fileId);
        final var resource = objectStorageServiceOutputPort.get(storedFile.getStoredResource().getStorageKey());
        storedFile.setStoredResource(resource);
        importUsersExecutorProvider.getExecutor(storedFile.getContentType()).execute(storedFile);
    }
}
