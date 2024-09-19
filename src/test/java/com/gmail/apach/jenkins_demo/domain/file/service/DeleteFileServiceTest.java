package com.gmail.apach.jenkins_demo.domain.file.service;

import com.gmail.apach.jenkins_demo.application.output.file.DeleteFileOutputPort;
import com.gmail.apach.jenkins_demo.application.output.file.GetFileOutputPort;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import com.gmail.apach.jenkins_demo.common.util.AwsS3Util;
import com.gmail.apach.jenkins_demo.data.FilesTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteFileServiceTest {

    private static final String FILE_ID = "qqed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private DeleteFileService deleteFileService;
    @Mock
    private GetFileOutputPort getFileOutputPort;
    @Mock
    private DeleteFileOutputPort deleteFileOutputPort;
    @Mock
    private AwsS3Util awsS3Util;

    @Test
    void deleteFile_success() {
        final var storedFile = FilesTestData.storedFile();
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        doNothing().when(awsS3Util).delete(storedFile.getStorageKey());
        doNothing().when(deleteFileOutputPort).deleteFile(storedFile.getFileId());

        assertDoesNotThrow(() -> deleteFileService.deleteFile(storedFile.getFileId()));
    }

    @Test
    void deleteFile_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getFileOutputPort).getByFileId(FILE_ID);

        assertThrows(ResourceNotFoundException.class, () -> deleteFileService.deleteFile(FILE_ID));
    }
}