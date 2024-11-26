package com.gmail.apach.hexagonaltemplate.infrastructure.output.auth;

import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.UnauthorizedException;
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
public class CurrentPrincipalAdapter implements CurrentPrincipalOutputPort {

    private final MessageSource messageSource;

    @Override
    public User getPrincipal() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new UnauthorizedException(
                messageSource.getMessage(Error.UNAUTHORISED.getKey(), null, LocaleContextHolder.getLocale()));
        }

        final var holderAuthorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(holderAuthorities)) {
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_AUTHORITIES_NOT_FOUND.getKey(), null, LocaleContextHolder.getLocale()));
        }
        final var authorities = holderAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        final var roles = authorities.stream()
            .map(authority -> Role.builder().role(RoleType.from(authority)).build())
            .collect(Collectors.toSet());

        return User.builder().username(authentication.getName()).roles(roles).build();
    }
}
