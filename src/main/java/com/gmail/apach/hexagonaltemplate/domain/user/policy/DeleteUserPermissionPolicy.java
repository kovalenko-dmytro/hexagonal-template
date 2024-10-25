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
public class DeleteUserPermissionPolicy {

    private final MessageSource messageSource;

    public void check(User deletedUser) {
        if (!AuthPrincipal.getDetails().isAdmin() || deletedUser.isAdmin()) {
            final var args = new Object[]{deletedUser.getUserId()};
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_USER_DELETE_BY_ID.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }
}
