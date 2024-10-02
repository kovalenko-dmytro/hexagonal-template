package com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper;

import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import lombok.Builder;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;

@Builder
public record CurrentUserContext(
    String username,
    Collection<String> authorities
) {
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
