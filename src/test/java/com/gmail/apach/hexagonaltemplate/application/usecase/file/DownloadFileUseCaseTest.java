package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.AwsS3StorageServiceAdapter;
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
class DownloadFileUseCaseTest {

    private static final String FILE_ID = "qqed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private DownloadFileUseCase downloadFileUseCase;
    @Mock
    private GetFileOutputPort getFileOutputPort;
    @Mock
    private AwsS3StorageServiceAdapter awsS3Adapter;
    @Mock
    private MessageSource messageSource;

    @Test
    void downloadFile_success() throws IOException {
        final var storedFile = FilesTestData.storedFile();

        final var s3Resource = mock(S3Resource.class);
        final var is = mock(ByteArrayInputStream.class);
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        when(awsS3Adapter.get(storedFile.getStorageKey())).thenReturn(s3Resource);
        when(s3Resource.getInputStream()).thenReturn(is);
        when(is.readAllBytes()).thenReturn(any());

        assertDoesNotThrow(() -> downloadFileUseCase.downloadFile(FILE_ID));
    }

    @Test
    void downloadFile_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getFileOutputPort).getByFileId(FILE_ID);

        assertThrows(ResourceNotFoundException.class, () -> downloadFileUseCase.downloadFile(FILE_ID));
    }

    @Test
    void downloadFile_cantReadContent() throws IOException {
        final var storedFile = FilesTestData.storedFile();

        final var s3Resource = mock(S3Resource.class);
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        when(awsS3Adapter.get(storedFile.getStorageKey())).thenReturn(s3Resource);
        doThrow(new IOException("readError"))
            .when(s3Resource).getInputStream();
        when(messageSource.getMessage(any(), any(), any())).thenReturn("error");

        assertThrows(ApplicationServerException.class, () -> downloadFileUseCase.downloadFile(FILE_ID));
    }
}