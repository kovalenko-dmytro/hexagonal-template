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
public class GetUserByIdPermissionPolicy {

    private final MessageSource messageSource;

    public void check(User user) {
        final var authPrincipal = AuthPrincipal.getDetails();

        final var isAdmin = authPrincipal.isAdmin();
        final var managerTriesToGetAnyExceptAdmin = authPrincipal.isManager() && !user.isAdmin();
        final var userTriesToGetSelf = authPrincipal.isUser() && user.isSelf(authPrincipal);

        if (!(isAdmin || managerTriesToGetAnyExceptAdmin || userTriesToGetSelf)) {
            final var args = new Object[]{user.getUserId()};
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_USER_GET_BY_ID.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }
}
