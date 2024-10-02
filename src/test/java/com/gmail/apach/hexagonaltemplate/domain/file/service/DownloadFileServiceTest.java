package com.gmail.apach.hexagonaltemplate.domain.file.service;

import com.gmail.apach.hexagonaltemplate.application.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.AwsS3Util;
import io.awspring.cloud.s3.S3Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DownloadFileServiceTest {

    private static final String FILE_ID = "qqed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private DownloadFileService downloadFileService;
    @Mock
    private GetFileOutputPort getFileOutputPort;
    @Mock
    private AwsS3Util awsS3Util;
    @Mock
    private MessageSource messageSource;

    @Test
    void downloadFile_success() throws IOException {
        final var storedFile = FilesTestData.storedFile();

        final var s3Resource = mock(S3Resource.class);
        final var is = mock(ByteArrayInputStream.class);
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        when(awsS3Util.get(storedFile.getStorageKey())).thenReturn(s3Resource);
        when(s3Resource.getInputStream()).thenReturn(is);
        when(is.readAllBytes()).thenReturn(any());

        assertDoesNotThrow(() -> downloadFileService.downloadFile(FILE_ID));
    }

    @Test
    void downloadFile_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getFileOutputPort).getByFileId(FILE_ID);

        assertThrows(ResourceNotFoundException.class, () -> downloadFileService.downloadFile(FILE_ID));
    }

    @Test
    void downloadFile_cantReadContent() throws IOException {
        final var storedFile = FilesTestData.storedFile();

        final var s3Resource = mock(S3Resource.class);
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        when(awsS3Util.get(storedFile.getStorageKey())).thenReturn(s3Resource);
        doThrow(new IOException("readError"))
            .when(s3Resource).getInputStream();

        assertThrows(ApplicationServerException.class, () -> downloadFileService.downloadFile(FILE_ID));
    }
}