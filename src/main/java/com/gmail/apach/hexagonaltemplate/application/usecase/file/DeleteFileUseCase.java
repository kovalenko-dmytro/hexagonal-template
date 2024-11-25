package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.DeleteFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishFileOutputPort;
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
        deleteFileOutputPort.deleteByFileId(file.getFileId());
        publishFileOutputPort.publishDelete(file.getStoredResource().getStorageKey());
    }
}
