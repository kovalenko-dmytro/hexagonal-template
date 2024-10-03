package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserAuthContext;
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
        CurrentUserAuthContext currentUserAuthContext = currentUserContextUtil.getContext();

        final var requestRoleTypes = roles.stream()
            .map(Role::getRole)
            .collect(Collectors.toSet());

        final var isAdmin = currentUserAuthContext.isAdmin();
        final var isManagerCreateUser =
            currentUserAuthContext.isManager()
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
