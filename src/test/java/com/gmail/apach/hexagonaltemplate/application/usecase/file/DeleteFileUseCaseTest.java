package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.PublishFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteFileUseCaseTest {

    private static final String FILE_ID = "qqed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private DeleteFileUseCase deleteFileUseCase;
    @Mock
    private GetFileOutputPort getFileOutputPort;
    @Mock
    private DeleteFileOutputPort deleteFileOutputPort;
    @Mock
    private PublishFileOutputPort publishFileOutputPort;

    @Test
    void deleteByFileId_success() {
        final var storedFile = FilesTestData.storedFile();
        when(getFileOutputPort.getByFileId(FILE_ID)).thenReturn(storedFile);
        doNothing().when(deleteFileOutputPort).deleteFile(storedFile.getFileId());
        doNothing().when(publishFileOutputPort).publishDeleteFile(storedFile.getStorageKey());

        assertDoesNotThrow(() -> deleteFileUseCase.deleteByFileId(storedFile.getFileId()));
    }

    @Test
    void deleteByFileId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getFileOutputPort).getByFileId(FILE_ID);

        assertThrows(ResourceNotFoundException.class, () -> deleteFileUseCase.deleteByFileId(FILE_ID));
    }
}