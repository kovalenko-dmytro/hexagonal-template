package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateUserPermissionPolicy {

    private final MessageSource messageSource;

    public void check(User user) {
        final var authPrincipal = AuthPrincipal.getDetails();

        final var isAdmin = authPrincipal.isAdmin();
        final var isManagerCreateUser = authPrincipal.isManager() && user.isUser();

        if (!(isAdmin || isManagerCreateUser)) {
            final var args = new Object[]{user.getRoleTypesToStringJoining()};
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_USER_CREATION.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }
}
