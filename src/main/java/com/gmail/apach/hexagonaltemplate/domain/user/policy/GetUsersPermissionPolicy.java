package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUsersPermissionPolicy {

    private final MessageSource messageSource;

    public void check() {
        if (AuthPrincipal.getDetails().isUser()) {
            final var args = new Object[]{RoleType.USER.getAuthority()};
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_USER_GET_LIST.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }
}
