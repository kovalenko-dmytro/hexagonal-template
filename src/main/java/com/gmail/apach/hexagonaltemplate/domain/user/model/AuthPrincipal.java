package com.gmail.apach.hexagonaltemplate.domain.user.model;

import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.UnauthorizedException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class AuthPrincipal {

    private String username;
    private Collection<String> authorities;

    public static AuthPrincipal getDetails() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(authentication)) {
            throw new UnauthorizedException("Unauthorised");
        }

        final var holderAuthorities = authentication.getAuthorities();
        if (CollectionUtils.isEmpty(holderAuthorities)) {
            throw new ForbiddenException("There are no authorities found for current user");
        }
        final var authorities = holderAuthorities.stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toSet());

        return new AuthPrincipal(authentication.getName(), authorities);
    }

    public boolean isAdmin() {
        return CollectionUtils.isNotEmpty(authorities)
            && authorities.stream().anyMatch(authority -> authority.contentEquals(RoleType.ADMIN.getAuthority()));
    }

    public boolean isManager() {
        return CollectionUtils.isNotEmpty(authorities)
            && authorities.stream().noneMatch(authority -> authority.contentEquals(RoleType.ADMIN.getAuthority()))
            && authorities.stream().anyMatch(authority -> authority.contentEquals(RoleType.MANAGER.getAuthority()));
    }

    public boolean isUser() {
        return !isAdmin() && !isManager();
    }
}
