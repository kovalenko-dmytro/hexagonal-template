package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.GetUserByIdPermissionPolicy;
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
class GetUserInputPortTest {

    private static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @InjectMocks
    private GetUserInputPort getUserInputPort;
    @Mock
    private GetUserByIdPermissionPolicy getUserByIdPermissionPolicy;
    @Mock
    private GetUserOutputPort getUserOutputPort;

    @Test
    void getByUsername_success() {
        when(getUserOutputPort.getByUsername(USERNAME))
            .thenReturn(User.builder().username(USERNAME).build());

        final var actual = getUserInputPort.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    void getByUsername_fail() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUsername(USERNAME);

        assertThrows(ResourceNotFoundException.class, () -> getUserInputPort.getByUsername(USERNAME));
    }

    @Test
    void getByUserId_success() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);

        doNothing().when(getUserByIdPermissionPolicy).check(admin);

        final var actual = getUserInputPort.getByUserId(USER_ID);

        assertNotNull(actual);
    }

    @Test
    void getByUserId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> getUserInputPort.getByUserId(USER_ID));
    }

    @Test
    void getByUserId_forbidden() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);

        doThrow(new ForbiddenException("forbidden"))
            .when(getUserByIdPermissionPolicy).check(admin);

        assertThrows(ForbiddenException.class, () -> getUserInputPort.getByUserId(USER_ID));
    }
}