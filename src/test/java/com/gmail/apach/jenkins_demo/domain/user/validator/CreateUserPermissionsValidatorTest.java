package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateUserPermissionsValidatorTest {

    @InjectMocks
    private CreateUserPermissionsValidator createUserPermissionsValidator;
    @Mock
    private MessageSource messageSource;

    private final Set<Role> userRoles = Set.of(Role.builder().role(RoleType.USER).build());

    private final Set<Role> managerRoles =
        Set.of(Role.builder().role(RoleType.MANAGER).build(), Role.builder().role(RoleType.USER).build());

    private final Set<Role> adminRoles =
        Set.of(
            Role.builder().role(RoleType.ADMIN).build(),
            Role.builder().role(RoleType.MANAGER).build(),
            Role.builder().role(RoleType.USER).build());

    private final Collection<GrantedAuthority> adminAuthorities
        = List.of(
        new SimpleGrantedAuthority("ROLE_ADMIN"),
        new SimpleGrantedAuthority("ROLE_MANAGER"),
        new SimpleGrantedAuthority("ROLE_USER"));

    private final Collection<GrantedAuthority> managerAuthorities
        = List.of(new SimpleGrantedAuthority("ROLE_MANAGER"), new SimpleGrantedAuthority("ROLE_USER"));

    private final Collection<GrantedAuthority> userAuthorities
        = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    @Test
    void validateAdminCreatesUserOrManager_success() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(adminAuthorities);

        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(userRoles));
        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(managerRoles));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerCreatesUser_success() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(managerAuthorities);

        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(userRoles));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerCreatesManager_fail() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(managerAuthorities);

        assertThrows(ForbiddenException.class, () -> createUserPermissionsValidator.validate(managerRoles));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserCreatesAny_fail() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(userAuthorities);

        assertThrows(ForbiddenException.class, () -> createUserPermissionsValidator.validate(userRoles));
        assertThrows(ForbiddenException.class, () -> createUserPermissionsValidator.validate(managerRoles));
        assertThrows(ForbiddenException.class, () -> createUserPermissionsValidator.validate(adminRoles));

        Mockito.reset(authentication, securityContext);
    }
}