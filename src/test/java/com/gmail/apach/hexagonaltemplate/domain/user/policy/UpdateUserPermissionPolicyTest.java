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
class UpdateUserPermissionPolicyTest {

    @InjectMocks
    private UpdateUserPermissionPolicy updateUserPermissionPolicy;
    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminUpdateAnyUser_success() {
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
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateAdminUpdateOwnRolesAndEnabled_fail() {
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
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerUpdateSelfOrAnyUser_success() {
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

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerUpdateAnotherManagerOrAdmin_fail() {
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
            updateUserPermissionPolicy.check(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerUpdateEnabledToSelfOrAnotherManagerOrAdmin_fail() {
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

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerUpdateRoles_fail() {
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

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRoles()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserUpdateSelf_success() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("userCreatedByAdmin");
        when(authentication.getAuthorities()).thenReturn(authorities);

        assertDoesNotThrow(() ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAdmin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserUpdateNotSelf_fail() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(authorities);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserUpdateEnabledOrRoles_fail() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("user");
        when(authentication.getAuthorities()).thenReturn(authorities);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAdmin(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionPolicy.check(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        Mockito.reset(authentication, securityContext);
    }
}