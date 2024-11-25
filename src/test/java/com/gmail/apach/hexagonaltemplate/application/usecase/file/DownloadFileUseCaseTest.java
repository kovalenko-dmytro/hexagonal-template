package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.s3.AwsS3StorageServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Test
    void downloadFile_success() {
        final var storedFile = FilesTestData.storedFile();
        final var resource = mock(StoredResource.class);

        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        when(awsS3Adapter.get(storedFile.getStoredResource().getStorageKey())).thenReturn(resource);

        assertDoesNotThrow(() -> downloadFileUseCase.download(FILE_ID));
    }

    @Test
    void downloadFile_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getFileOutputPort).getByFileId(FILE_ID);

        assertThrows(ResourceNotFoundException.class, () -> downloadFileUseCase.download(FILE_ID));
    }
}