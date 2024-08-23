package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserPermissionsValidator {

    private final CurrentUserContextUtil currentUserContextUtil;
    private final MessageSource messageSource;

    public void validate(User deletedUser) {
        final var currentUserContext = currentUserContextUtil.getContext();

        if (!currentUserContext.isAdmin() || deletedUser.isAdmin()) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_USER_DELETE_BY_ID.getKey(),
                new Object[]{deletedUser.getUserId()},
                LocaleContextHolder.getLocale()));
        }
    }
}
