package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.DeleteFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.PublishFileOutputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteFileUseCase implements DeleteFileInputPort {

    private final GetFileOutputPort getFileOutputPort;
    private final DeleteFileOutputPort deleteFileOutputPort;
    private final PublishFileOutputPort publishFileOutputPort;

    @Override
    public void deleteByFileId(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        deleteFileOutputPort.deleteFile(file.getFileId());
        publishFileOutputPort.publishDeleteFile(file.getStorageKey());
    }
}
