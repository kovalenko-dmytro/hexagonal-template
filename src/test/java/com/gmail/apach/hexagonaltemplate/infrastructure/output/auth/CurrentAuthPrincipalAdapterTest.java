package com.gmail.apach.hexagonaltemplate.infrastructure.output.auth;

import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.UnauthorizedException;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentAuthPrincipalAdapterTest {

    private static final String USERNAME = "admin";
    @InjectMocks
    private CurrentPrincipalAdapter currentPrincipalAdapter;
    @Mock
    private MessageSource messageSource;

    @Test
    void getAuthPrincipal_success() {
        Collection<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(RoleType.ADMIN.getAuthority()),
            new SimpleGrantedAuthority(RoleType.MANAGER.getAuthority()),
            new SimpleGrantedAuthority(RoleType.USER.getAuthority())
        );

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(USERNAME);
        when(authentication.getAuthorities()).thenReturn(authorities);

        final var actual = currentPrincipalAdapter.getPrincipal();

        assertNotNull(actual);
        assertEquals(USERNAME, actual.getUsername());
        assertTrue(CollectionUtils.isNotEmpty(actual.getRoles()));
        final var roleTypes = actual.getRoles().stream()
                .map(Role::getRole)
                    .collect(Collectors.toSet());
        assertTrue(roleTypes.containsAll(Set.of(RoleType.ADMIN, RoleType.MANAGER, RoleType.USER)));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getAuthPrincipal_unauthorized() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UnauthorizedException.class, () -> currentPrincipalAdapter.getPrincipal());

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getAuthPrincipal_forbidden() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(USERNAME);
        when(authentication.getAuthorities()).thenReturn(null);

        assertThrows(ForbiddenException.class, () -> currentPrincipalAdapter.getPrincipal());

        Mockito.reset(authentication, securityContext);
    }
}