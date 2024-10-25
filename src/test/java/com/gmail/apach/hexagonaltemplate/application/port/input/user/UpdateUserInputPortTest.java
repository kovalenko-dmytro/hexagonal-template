package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.UpdateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.UpdateUserPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserInputPortTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512433";

    @InjectMocks
    UpdateUserInputPort updateUserInputPort;
    @Mock
    private GetUserOutputPort getUserOutputPort;
    @Mock
    private UpdateUserOutputPort updateUserOutputPort;
    @Mock
    private UpdateUserPermissionPolicy updateUserPermissionPolicy;

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
            .when(updateUserPermissionPolicy)
            .check(user, updatedData);
        when(updateUserOutputPort.update(user))
            .thenReturn(updatedUser);

        final var actual = updateUserInputPort.update(updatedData);

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

        assertThrows(ResourceNotFoundException.class, () -> updateUserInputPort.update(updatedData));
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
            .when(updateUserPermissionPolicy).check(user, updatedData);

        assertThrows(ForbiddenException.class, () -> updateUserInputPort.update(updatedData));
    }
}