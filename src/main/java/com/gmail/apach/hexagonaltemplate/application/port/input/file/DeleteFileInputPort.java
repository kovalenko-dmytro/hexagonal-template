package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.PublishFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.file.DeleteFileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteFileInputPort implements DeleteFileUseCase {

    private final GetFileOutputPort getFileOutputPort;
    private final DeleteFileOutputPort deleteFileOutputPort;
    private final PublishFileOutputPort publishFileOutputPort;

    @Override
    public void deleteFile(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        deleteFileOutputPort.deleteFile(file.getFileId());
        publishFileOutputPort.publishDeleteFile(file.getStorageKey());
    }
}
