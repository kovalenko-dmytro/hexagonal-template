package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.exception.EntityNotFoundException;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserServiceTest {

    private static final String USERNAME = "username";
    private static final String ERROR_MESSAGE = "No User entity with username username exists!";

    @InjectMocks
    private GetUserService getUserService;
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
}