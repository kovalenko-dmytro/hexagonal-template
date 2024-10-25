package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserPermissionPolicyTest {

    @InjectMocks
    private DeleteUserPermissionPolicy deleteUserPermissionPolicy;
    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminDeleteAnyOtherUsers_success() {
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

        assertDoesNotThrow(() ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByAdmin()));
        assertDoesNotThrow(() ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByManager()));
        assertDoesNotThrow(() ->
            deleteUserPermissionPolicy.check(UsersTestData.manager()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateAdminDeleteSelf_fail() {
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
        when(messageSource.getMessage(any(), any(), any())).thenReturn("error");

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.admin()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerDeleteAnyOtherUsers_fail() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.MANAGER.getAuthority()),
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("manager");
        when(authentication.getAuthorities()).thenReturn(authorities);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("error");

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByAdmin()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByAnotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.manager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.admin()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserDeleteAnyOtherUsers_fail() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(authorities);
        when(messageSource.getMessage(any(), any(), any())).thenReturn("error");

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByAdmin()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.userCreatedByAnotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.manager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionPolicy.check(UsersTestData.admin()));

        Mockito.reset(authentication, securityContext);
    }
}