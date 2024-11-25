package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.UploadFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.CreateFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.StorageServiceProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UploadFileUseCase implements UploadFileInputPort {

    @Qualifier(StorageServiceProviderType.S3)
    private final ObjectStorageServiceOutputPort objectStorageServiceOutputPort;
    private final CreateFileOutputPort createFileOutputPort;

    @Override
    public StoredFile upload(MultipartFile file) {
        final var storedResource = objectStorageServiceOutputPort.save(file);

        final var storedFile = StoredFile.builder()
            .storedResource(storedResource).fileName(file.getOriginalFilename())
            .contentType(file.getContentType()).size(file.getSize()).created(LocalDateTime.now())
            .build();

        return createFileOutputPort.create(storedFile);
    }
}
