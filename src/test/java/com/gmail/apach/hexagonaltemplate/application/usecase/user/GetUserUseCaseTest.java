package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    private static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @InjectMocks
    private GetUserUseCase getUserUseCase;
    @Mock
    private CurrentPrincipalOutputPort currentPrincipalOutputPort;
    @Mock
    private GetUserOutputPort getUserOutputPort;

    @Test
    void getByUsername_success() {
        when(getUserOutputPort.getByUsername(USERNAME))
            .thenReturn(User.builder().username(USERNAME).build());

        final var actual = getUserUseCase.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    void getByUsername_fail() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUsername(USERNAME);

        assertThrows(ResourceNotFoundException.class, () -> getUserUseCase.getByUsername(USERNAME));
    }

    @Test
    void getByUserId_success() {
        final var admin = UsersTestData.admin();

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);
        when(currentPrincipalOutputPort.getPrincipal()).thenReturn(UsersTestData.admin());

        final var actual = getUserUseCase.getByUserId(USER_ID);

        assertNotNull(actual);
    }

    @Test
    void getByUserId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> getUserUseCase.getByUserId(USER_ID));
    }

    @Test
    void getByUserId_forbidden() {
        final var admin = UsersTestData.admin();

        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);
        when(currentPrincipalOutputPort.getPrincipal()).thenReturn(UsersTestData.userCreatedByAdmin());

        assertThrows(PolicyViolationException.class, () -> getUserUseCase.getByUserId(USER_ID));
    }
}