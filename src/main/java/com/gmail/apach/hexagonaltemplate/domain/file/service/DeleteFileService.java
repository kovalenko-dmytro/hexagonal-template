package com.gmail.apach.hexagonaltemplate.domain.file.service;

import com.gmail.apach.hexagonaltemplate.application.input.file.DeleteFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.AwsS3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteFileService implements DeleteFileInputPort {

    private final GetFileOutputPort getFileOutputPort;
    private final DeleteFileOutputPort deleteFileOutputPort;
    private final AwsS3Util awsS3Util;

    @Override
    public void deleteFile(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        awsS3Util.delete(file.getStorageKey());
        deleteFileOutputPort.deleteFile(fileId);
    }
}
