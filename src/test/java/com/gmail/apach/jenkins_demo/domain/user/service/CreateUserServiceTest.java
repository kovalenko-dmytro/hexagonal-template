package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.CreateUserPermissionsValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserServiceTest {

    @InjectMocks
    private CreateUserService createUserService;
    @Mock
    private CreateUserOutputPort createUserOutputPort;
    @Mock
    private CreateUserPermissionsValidator createUserPermissionsValidator;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final User user = User.builder()
        .username("username")
        .password("password")
        .email("email@email.com")
        .firstName("firstName")
        .lastName("lastName")
        .roles(Set.of(Role.builder().role(RoleType.USER).build()))
        .build();

    private final User userWithNoRoles = User.builder()
        .username("username")
        .password("password")
        .email("email@email.com")
        .firstName("firstName")
        .lastName("lastName")
        .build();

    private final User savedUser = User.builder()
        .userId("6a8d68c8-2f28-4b53-ac5a-2db586512438")
        .username("username")
        .password("encodedPassword")
        .email("email@email.com")
        .firstName("firstName")
        .lastName("lastName")
        .roles(Set.of(Role.builder().roleId("6a8d68c8-2f28-4b53-ac5a-2db586512437").role(RoleType.USER).build()))
        .build();

    @Test
    void createUser_success() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        doNothing().when(createUserPermissionsValidator).validate(user.getRoles());
        when(createUserOutputPort.createUser(user)).thenReturn(savedUser);

        final var actual = createUserService.createUser(user);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
    }

    @Test
    void createUserWithNoRoles_success() {
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(createUserOutputPort.createUser(userWithNoRoles)).thenReturn(savedUser);

        final var actual = createUserService.createUser(userWithNoRoles);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertFalse(actual.getRoles().isEmpty());
        assertEquals(1, actual.getRoles().size());
        final var roleTypes = actual.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        assertTrue(roleTypes.contains(RoleType.USER));
    }

    @Test
    void createUser_fail() {
        doThrow(new ForbiddenException("forbidden"))
            .when(createUserPermissionsValidator).validate(user.getRoles());

        assertThrows(ForbiddenException.class, () -> createUserService.createUser(user));
    }
}