package com.gmail.apach.jenkins_demo.common.util;

import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.exception.UnauthorizedException;
import com.gmail.apach.jenkins_demo.domain.user.model.RoleType;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrentUserContextUtilTest {

    private static final String USERNAME = "admin";

    @InjectMocks
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private MessageSource messageSource;

    @Test
    void getContext_success() {
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

        final var actual = currentUserContextUtil.getContext();

        assertNotNull(actual);
        assertEquals(USERNAME, actual.username());
        assertTrue(CollectionUtils.isNotEmpty(actual.authorities()));
        assertTrue(actual.authorities().containsAll(
            Set.of(
                RoleType.ADMIN.getAuthority(),
                RoleType.MANAGER.getAuthority(),
                RoleType.USER.getAuthority())));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getContext_unauthorized() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        assertThrows(UnauthorizedException.class, () -> currentUserContextUtil.getContext());

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getContext_forbidden() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(USERNAME);
        when(authentication.getAuthorities()).thenReturn(null);

        assertThrows(ForbiddenException.class, () -> currentUserContextUtil.getContext());

        Mockito.reset(authentication, securityContext);
    }
}