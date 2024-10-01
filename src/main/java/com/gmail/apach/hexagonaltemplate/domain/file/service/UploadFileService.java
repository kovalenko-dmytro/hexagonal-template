package com.gmail.apach.hexagonaltemplate.domain.file.service;

import com.gmail.apach.hexagonaltemplate.application.input.file.UploadFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.file.CreateFileOutputPort;
import com.gmail.apach.hexagonaltemplate.common.util.AwsS3Util;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UploadFileService implements UploadFileInputPort {

    private final AwsS3Util awsS3Util;
    private final CreateFileOutputPort createFileOutputPort;

    @Override
    public StoredFile uploadFile(MultipartFile file) {
        final var savedResource = awsS3Util.save(file);

        final var storedFile = StoredFile.builder()
            .storageKey(savedResource.getLocation().getObject())
            .fileName(file.getOriginalFilename())
            .contentType(file.getContentType())
            .size(file.getSize())
            .created(LocalDateTime.now())
            .build();

        return createFileOutputPort.createFile(storedFile);
    }
}
