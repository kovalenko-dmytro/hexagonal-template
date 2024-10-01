package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.application.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.output.user.UpdateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.validator.UpdateUserPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserServiceTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512433";

    @InjectMocks
    UpdateUserService updateUserService;

    @Mock
    private GetUserOutputPort getUserOutputPort;
    @Mock
    private UpdateUserOutputPort updateUserOutputPort;
    @Mock
    private UpdateUserPermissionsValidator updateUserPermissionsValidator;

    @Test
    void update_success() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);
        final var updatedData = UsersTestData.updateUserDataWithoutNewRolesAndEnabled();
        updatedData.setUserId(USER_ID);
        final var updatedUser = UsersTestData.updatedUserCreatedByAdmin();
        updatedUser.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID))
            .thenReturn(user);
        doNothing()
            .when(updateUserPermissionsValidator)
            .validate(user, updatedData);
        when(updateUserOutputPort.update(user))
            .thenReturn(updatedUser);

        final var actual = updateUserService.update(updatedData);

        assertNotNull(actual);
        assertEquals(USER_ID, actual.getUserId());
        assertEquals(UsersTestData.updatedUserCreatedByAdmin().getFirstName(), actual.getFirstName());
        assertEquals(UsersTestData.updatedUserCreatedByAdmin().getEmail(), actual.getEmail());
        assertEquals(UsersTestData.updatedUserCreatedByAdmin().getEnabled(), actual.getEnabled());
    }

    @Test
    void update_notFound() {
        final var updatedData = UsersTestData.updateUserDataWithoutNewRolesAndEnabled();
        updatedData.setUserId(USER_ID);

        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> updateUserService.update(updatedData));
    }

    @Test
    void update_forbidden() {
        final var user = UsersTestData.userCreatedByAdmin();
        user.setUserId(USER_ID);
        final var updatedData = UsersTestData.updateUserDataWithoutNewRolesAndEnabled();
        updatedData.setUserId(USER_ID);

        when(getUserOutputPort.getByUserId(USER_ID))
            .thenReturn(user);
        doThrow(new ForbiddenException("forbidden"))
            .when(updateUserPermissionsValidator).validate(user, updatedData);

        assertThrows(ForbiddenException.class, () -> updateUserService.update(updatedData));
    }
}