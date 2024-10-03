package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserAuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUsersPermissionsValidator {

    private final MessageSource messageSource;

    public void validate(CurrentUserAuthContext context) {
        if (context.isUser()) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_USER_GET_LIST.getKey(),
                new Object[]{RoleType.USER.getAuthority()},
                LocaleContextHolder.getLocale()));
        }
    }
}
