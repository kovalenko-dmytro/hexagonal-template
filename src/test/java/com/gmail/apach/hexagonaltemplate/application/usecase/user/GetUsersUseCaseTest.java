package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.GetUsersPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersUseCaseTest {

    @InjectMocks
    private GetUsersUseCase getUsersUseCase;
    @Mock
    private GetUsersPermissionPolicy getUsersPermissionPolicy;
    @Mock
    private GetUsersOutputPort getUsersOutputPort;

    @Test
    void getUsers_success() {
        final var users = new PageImpl<>(List.of(UsersTestData.admin(), UsersTestData.userCreatedByAdmin()));
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

        doNothing().when(getUsersPermissionPolicy).check();

        when(getUsersOutputPort.getUsers(any(GetUsersFilterWrapper.class))).thenReturn(users);

        final var actual = getUsersUseCase.getUsers(null, null, null, null,
            null, null, null, null, 1, 1, new String[]{"sendBy"});

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getUsers_forbidden() {
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
            .when(getUsersPermissionPolicy).check();

        assertThrows(ForbiddenException.class, () ->
            getUsersUseCase.getUsers(null, null, null, null, null,
                null, null, null, 1, 1, new String[]{"sendBy"}));

        Mockito.reset(authentication, securityContext);
    }
}