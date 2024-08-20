package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetUserByIdPermissionsValidator {

    private final MessageSource messageSource;

    public void validate(User user) {
        final var authUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        final var authAuthorities = SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        final var isAdmin = authAuthorities.contains("ROLE_ADMIN");

        final var isManagerTriesToGetSelfOrOwnUsers = !authAuthorities.contains("ROLE_ADMIN")
            && authAuthorities.contains("ROLE_MANAGER")
            && (user.getUsername().contentEquals(authUsername) || user.getCreatedBy().contentEquals(authUsername));

        final var isUserTriesToGetSelf = !authAuthorities.contains("ROLE_ADMIN")
            && !authAuthorities.contains("ROLE_MANAGER")
            && authAuthorities.contains("ROLE_USER")
            && user.getUsername().contentEquals(authUsername);

        if (!(isAdmin || isManagerTriesToGetSelfOrOwnUsers || isUserTriesToGetSelf)) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_USER_GET_BY_ID.getKey(),
                new Object[]{user.getUserId()},
                LocaleContextHolder.getLocale()));
        }
    }
}
