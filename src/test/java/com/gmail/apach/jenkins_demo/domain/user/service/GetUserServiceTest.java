package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.exception.EntityNotFoundException;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.data.UsersTestData;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.GetUserByIdPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    private static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";
    private static final String ERROR_MESSAGE = "No User entity with username username exists!";

    @InjectMocks
    private GetUserService getUserService;
    @Mock
    private GetUserByIdPermissionsValidator getUserByIdPermissionsValidator;
    @Mock
    private GetUserOutputPort getUserOutputPort;
    @Mock
    private MessageSource messageSource;

    @Test
    void getByUsername_success() {
        when(getUserOutputPort.getByUsername(USERNAME))
            .thenReturn(Optional.of(User.builder().username(USERNAME).build()));

        final var actual = getUserService.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    void getByUsername_fail() {
        when(getUserOutputPort.getByUsername(USERNAME))
            .thenReturn(Optional.empty());
        when(messageSource.getMessage(any(), any(), any()))
            .thenReturn(ERROR_MESSAGE);

        final var exception = assertThrows(
            EntityNotFoundException.class, () -> getUserService.getByUsername(USERNAME));

        assertTrue(exception.getMessage().contains(ERROR_MESSAGE));
    }

    @Test
    void getByUserId_success() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(Optional.ofNullable(admin));

        final var authentication = mock(Authentication.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("admin");
        doNothing().when(getUserByIdPermissionsValidator).validate(admin);

        final var actual = getUserService.getByUserId(USER_ID);

        assertNotNull(actual);

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getByUserId_notFound() {
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> getUserService.getByUserId(USER_ID));
    }

    @Test
    void getByUserId_forbidden() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(Optional.of(admin));

        final var authentication = mock(Authentication.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");
        doThrow(new ForbiddenException("forbidden"))
            .when(getUserByIdPermissionsValidator).validate(admin);

        assertThrows(ForbiddenException.class, () -> getUserService.getByUserId(USER_ID));

        Mockito.reset(authentication, securityContext);
    }
}