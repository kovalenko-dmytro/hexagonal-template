package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.ImportUsersFromFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportUsersFromFileUseCaseTest {

    private static final String USERNAME = "username";
    private static final String FILE_ID = "qqed68c8-2f28-4b53-ac5a-2db586512eee";

    @InjectMocks
    private ImportUsersFromFileUseCase importUsersFromFileUseCase;
    @Mock
    private CurrentPrincipalOutputPort currentPrincipalOutputPort;
    @Mock
    private ImportUsersFromFileOutputPort importUsersFromFileOutputPort;

    @Test
    void execute_success() {
        when(currentPrincipalOutputPort.getPrincipal())
            .thenReturn(UsersTestData.admin());

        final var actual = importUsersFromFileUseCase.execute(FILE_ID);

        assertNotNull(actual);
        assertNotNull(actual.getBatchId());
    }
}