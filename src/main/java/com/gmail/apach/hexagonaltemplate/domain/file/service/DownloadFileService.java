package com.gmail.apach.hexagonaltemplate.domain.file.service;

import com.gmail.apach.hexagonaltemplate.application.input.file.DownloadFileInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.common.constant.message.Error;
import com.gmail.apach.hexagonaltemplate.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.common.util.AwsS3Util;
import com.gmail.apach.hexagonaltemplate.domain.file.wrapper.FilePayloadWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class DownloadFileService implements DownloadFileInputPort {

    private final GetFileOutputPort getFileOutputPort;
    private final AwsS3Util awsS3Util;
    private final MessageSource messageSource;

    @Override
    public FilePayloadWrapper downloadFile(String fileId) {
        final var file = getFileOutputPort.getByFileId(fileId);
        final var s3Resource = awsS3Util.get(file.getStorageKey());
        try {
            return FilePayloadWrapper.builder()
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
