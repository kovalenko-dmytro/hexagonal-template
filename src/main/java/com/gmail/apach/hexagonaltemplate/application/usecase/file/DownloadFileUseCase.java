package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.DownloadFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.StorageServiceProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadFileUseCase implements DownloadFileInputPort {

    private final GetFileOutputPort getFileOutputPort;
    @Qualifier(StorageServiceProviderType.S3)
    private final ObjectStorageServiceOutputPort objectStorageServiceOutputPort;

    @Override
    public StoredFile download(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        final var storedResource = objectStorageServiceOutputPort.get(file.getStoredResource().getStorageKey());
        file.setStoredResource(storedResource);
        return file;
    }
}
