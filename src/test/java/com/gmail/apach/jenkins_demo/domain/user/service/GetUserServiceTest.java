package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import com.gmail.apach.jenkins_demo.data.UsersTestData;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.GetUserByIdPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    private static final String USERNAME = "username";
    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

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
            .thenReturn(User.builder().username(USERNAME).build());

        final var actual = getUserService.getByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
    }

    @Test
    void getByUsername_fail() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUsername(USERNAME);

        assertThrows(ResourceNotFoundException.class, () -> getUserService.getByUsername(USERNAME));
    }

    @Test
    void getByUserId_success() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);

        doNothing().when(getUserByIdPermissionsValidator).validate(admin);

        final var actual = getUserService.getByUserId(USER_ID);

        assertNotNull(actual);
    }

    @Test
    void getByUserId_notFound() {
        doThrow(new ResourceNotFoundException("notFound"))
            .when(getUserOutputPort).getByUserId(USER_ID);

        assertThrows(ResourceNotFoundException.class, () -> getUserService.getByUserId(USER_ID));
    }

    @Test
    void getByUserId_forbidden() {
        final var admin = UsersTestData.admin();
        when(getUserOutputPort.getByUserId(USER_ID)).thenReturn(admin);

        doThrow(new ForbiddenException("forbidden"))
            .when(getUserByIdPermissionsValidator).validate(admin);

        assertThrows(ForbiddenException.class, () -> getUserService.getByUserId(USER_ID));
    }
}