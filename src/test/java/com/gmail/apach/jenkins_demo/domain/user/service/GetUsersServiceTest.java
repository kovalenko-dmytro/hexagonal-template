package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.output.user.GetUsersOutputPort;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.data.AuthoritiesTestData;
import com.gmail.apach.jenkins_demo.data.UsersTestData;
import com.gmail.apach.jenkins_demo.domain.user.validator.GetUsersPermissionsValidator;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUsersServiceTest {

    @InjectMocks
    private GetUsersService getUsersService;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private GetUsersPermissionsValidator getUsersPermissionsValidator;
    @Mock
    private GetUsersOutputPort getUsersOutputPort;

    @Test
    void getUsers_success() {
        final var users = new PageImpl<>(List.of(UsersTestData.admin(), UsersTestData.userCreatedByAdmin()));
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();
        final var wrapper = GetUsersRequestWrapper.builder().build();

        when(currentUserContextUtil.getContext())
            .thenReturn(context);
        doNothing()
            .when(getUsersPermissionsValidator)
            .validate(context);

        when(getUsersOutputPort.getUsers(wrapper, context))
            .thenReturn(users);

        final var actual = getUsersService.getUsers(wrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
    }

    @Test
    void getUsers_forbidden() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();
        final var wrapper = GetUsersRequestWrapper.builder().build();

        when(currentUserContextUtil.getContext())
            .thenReturn(context);
        doThrow(new ForbiddenException("forbidden"))
            .when(getUsersPermissionsValidator).validate(context);

        assertThrows(ForbiddenException.class, () -> getUsersService.getUsers(wrapper));
    }
}