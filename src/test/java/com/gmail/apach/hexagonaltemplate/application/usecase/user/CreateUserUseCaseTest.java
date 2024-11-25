package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.data.CreateUserTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.CreateUserPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @InjectMocks
    private CreateUserUseCase createUserUseCase;
    @Mock
    private CreateUserOutputPort createUserOutputPort;
    @Mock
    private CreateUserPermissionPolicy createUserPermissionPolicy;
    @Mock
    private PublishEmailOutputPort publishEmailOutputPort;

    @Test
    void createUser_success() {
        final var user = CreateUserTestData.user();
        final var savedUser = CreateUserTestData.savedUser();

        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.ADMIN.getAuthority()),
            new SimpleGrantedAuthority(RoleType.MANAGER.getAuthority()),
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("admin");
        when(authentication.getAuthorities()).thenReturn(authorities);

        doNothing().when(createUserPermissionPolicy).check(user);
        when(createUserOutputPort.create(user)).thenReturn(savedUser);

        final var actual = createUserUseCase.create(user);

        verify(publishEmailOutputPort, times(1)).publishSend(any(SendEmailWrapper.class));
        assertNotNull(actual);
        assertNotNull(actual.getUserId());

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void createUserWithNoRoles_success() {
        final var userWithNoRoles = CreateUserTestData.userWithNoRoles();
        final var savedUser = CreateUserTestData.savedUser();

        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.ADMIN.getAuthority()),
            new SimpleGrantedAuthority(RoleType.MANAGER.getAuthority()),
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("admin");
        when(authentication.getAuthorities()).thenReturn(authorities);

        when(createUserOutputPort.create(userWithNoRoles)).thenReturn(savedUser);

        final var actual = createUserUseCase.create(userWithNoRoles);

        verify(publishEmailOutputPort, times(1)).publishSend(any(SendEmailWrapper.class));
        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertFalse(actual.getRoles().isEmpty());
        assertEquals(1, actual.getRoles().size());
        final var roleTypes = actual.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        assertTrue(roleTypes.contains(RoleType.USER));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void createUser_forbidden() {
        final var user = CreateUserTestData.user();

        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(authorities);

        doThrow(new ForbiddenException("forbidden"))
            .when(createUserPermissionPolicy).check(user);

        assertThrows(ForbiddenException.class, () -> createUserUseCase.create(user));

        Mockito.reset(authentication, securityContext);
    }
}