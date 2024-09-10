package com.gmail.apach.jenkins_demo.common.util;

import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrentUserContextUtil {

    private final MessageSource messageSource;

    public CurrentUserContext getContext() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new UnauthorizedException(messageSource.getMessage(
                Error.UNAUTHORISED.getKey(), null, LocaleContextHolder.getLocale()));
        }

        final var authorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(authorities)) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_AUTHORITIES_NOT_FOUND.getKey(), null, LocaleContextHolder.getLocale()));
        }
        return CurrentUserContext.builder()
            .username(authentication.getName())
            .authorities(
                authorities
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet()))
            .build();
    }
}
