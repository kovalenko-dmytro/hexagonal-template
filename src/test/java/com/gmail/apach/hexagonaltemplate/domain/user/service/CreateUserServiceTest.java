package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.application.output.email.SendEmailPublisher;
import com.gmail.apach.hexagonaltemplate.application.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.common.dto.CurrentUserContext;
import com.gmail.apach.hexagonaltemplate.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.data.CreateUserTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.domain.user.validator.CreateUserPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final String CURRENT_USER_USERNAME = "admin";

    @InjectMocks
    private CreateUserService createUserService;
    @Mock
    private CreateUserOutputPort createUserOutputPort;
    @Mock
    private CreateUserPermissionsValidator createUserPermissionsValidator;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SendEmailPublisher sendEmailPublisher;

    @Test
    void createUser_success() {
        final var user = CreateUserTestData.user();
        final var savedUser = CreateUserTestData.savedUser();
        final var context = CurrentUserContext.builder()
            .username(CURRENT_USER_USERNAME)
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(currentUserContextUtil.getContext()).thenReturn(context);
        doNothing().when(createUserPermissionsValidator).validate(user.getRoles());
        when(createUserOutputPort.createUser(user)).thenReturn(savedUser);

        final var actual = createUserService.createUser(user);

        verify(sendEmailPublisher, times(1)).publishInviteEmail(actual);
        assertNotNull(actual);
        assertNotNull(actual.getUserId());
    }

    @Test
    void createUserWithNoRoles_success() {
        final var user = CreateUserTestData.user();
        final var userWithNoRoles = CreateUserTestData.userWithNoRoles();
        final var savedUser = CreateUserTestData.savedUser();
        final var context = CurrentUserContext.builder()
            .username(CURRENT_USER_USERNAME)
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(currentUserContextUtil.getContext()).thenReturn(context);
        when(createUserOutputPort.createUser(userWithNoRoles)).thenReturn(savedUser);

        final var actual = createUserService.createUser(userWithNoRoles);

        verify(sendEmailPublisher, times(1)).publishInviteEmail(actual);
        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertFalse(actual.getRoles().isEmpty());
        assertEquals(1, actual.getRoles().size());
        final var roleTypes = actual.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        assertTrue(roleTypes.contains(RoleType.USER));
    }

    @Test
    void createUser_forbidden() {
        final var user = CreateUserTestData.user();
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);
        doThrow(new ForbiddenException("forbidden"))
            .when(createUserPermissionsValidator).validate(user.getRoles());

        assertThrows(ForbiddenException.class, () -> createUserService.createUser(user));
    }
}