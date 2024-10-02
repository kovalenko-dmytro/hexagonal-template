package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.application.output.user.DeleteUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.validator.DeleteUserPermissionsValidator;
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
class DeleteUserServiceTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @InjectMocks
    private DeleteUserService deleteUserService;
    @Mock
    private GetUserOutputPort getUserOutputPort;
    @Mock
    private DeleteUserOutputPort deleteUserOutputPort;
    @Mock
    private DeleteUserPermissionsValidator deleteUserPermissionsValidator;

    @Test
    void deleteByUserId_success() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(user);
        doNothing().when(deleteUserPermissionsValidator).validate(user);
        doNothing().when(deleteUserOutputPort).deleteByUserId(user.getUserId());

        assertDoesNotThrow(() -> deleteUserService.deleteByUserId(user.getUserId()));
    }

    @Test
    void deleteByUserId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> deleteUserService.deleteByUserId(USER_ID));
    }

    @Test
    void deleteByUserId_forbidden() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(user);
        doThrow(new ForbiddenException("forbidden"))
            .when(deleteUserPermissionsValidator).validate(user);

        assertThrows(ForbiddenException.class, () -> deleteUserService.deleteByUserId(USER_ID));
    }
}