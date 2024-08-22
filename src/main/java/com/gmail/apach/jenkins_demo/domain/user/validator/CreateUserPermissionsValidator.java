package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.constant.CommonConstant;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateUserPermissionsValidator {

    private final CurrentUserContextUtil currentUserContextUtil;
    private final MessageSource messageSource;

    public void validate(Set<Role> roles) {
        CurrentUserContext currentUserContext = currentUserContextUtil.getContext();

        final var requestRoleTypes = roles.stream()
            .map(Role::getRole)
            .collect(Collectors.toSet());

        final var isAdmin = currentUserContext.isAdmin();
        final var isManagerCreateUser =
            currentUserContext.isManager()
                && requestRoleTypes.contains(RoleType.USER)
                && !requestRoleTypes.contains(RoleType.MANAGER);
        if (!(isAdmin || isManagerCreateUser)) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_USER_CREATION.getKey(), getArgs(requestRoleTypes), LocaleContextHolder.getLocale()));
        }
    }

    private Object[] getArgs(Set<RoleType> requestRoleTypes) {
        return new Object[]{
            requestRoleTypes.stream()
                .map(RoleType::name)
                .collect(Collectors.joining(CommonConstant.COMMA.getValue()))};
    }
}
