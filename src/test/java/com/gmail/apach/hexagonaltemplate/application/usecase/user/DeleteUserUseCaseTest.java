package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.DeleteUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.DeleteUserPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
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
class DeleteUserUseCaseTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @InjectMocks
    private DeleteUserUseCase deleteUserUseCase;
    @Mock
    private GetUserOutputPort getUserOutputPort;
    @Mock
    private DeleteUserOutputPort deleteUserOutputPort;
    @Mock
    private DeleteUserPermissionPolicy deleteUserPermissionPolicy;

    @Test
    void deleteByUserId_success() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(user);
        doNothing().when(deleteUserPermissionPolicy).check(user);
        doNothing().when(deleteUserOutputPort).deleteByUserId(user.getUserId());

        assertDoesNotThrow(() -> deleteUserUseCase.deleteByUserId(user.getUserId()));
    }

    @Test
    void deleteByUserId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> deleteUserUseCase.deleteByUserId(USER_ID));
    }

    @Test
    void deleteByUserId_forbidden() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(user);
        doThrow(new ForbiddenException("forbidden"))
            .when(deleteUserPermissionPolicy).check(user);

        assertThrows(ForbiddenException.class, () -> deleteUserUseCase.deleteByUserId(USER_ID));
    }
}