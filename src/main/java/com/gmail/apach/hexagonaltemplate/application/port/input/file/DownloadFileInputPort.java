package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.file.DownloadFileUseCase;
import com.gmail.apach.hexagonaltemplate.domain.file.model.DownloadedFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DownloadFileInputPort implements DownloadFileUseCase {

    private final GetFileOutputPort getFileOutputPort;
    private final ObjectStorageServiceOutputPort objectStorageServiceOutputPort;
    private final MessageSource messageSource;

    @Override
    public DownloadedFile downloadFile(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        final var s3Resource = objectStorageServiceOutputPort.get(file.getStorageKey());
        try {
            return DownloadedFile.builder()
                .fileName(file.getFileName())
                .payload(s3Resource.getInputStream().readAllBytes())
                .build();
        } catch (IOException e) {
            final var args = new Object[]{file.getFileName(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_READ.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }
}
