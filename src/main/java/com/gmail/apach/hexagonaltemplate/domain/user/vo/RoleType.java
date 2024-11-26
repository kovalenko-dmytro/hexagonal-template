package com.gmail.apach.hexagonaltemplate.domain.user.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum RoleType {

    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    USER("ROLE_USER");

    private final String authority;
    private static final Map<String, RoleType> CACHE = new HashMap<>();

    static {
        for (var roleType : RoleType.values()) {
            CACHE.put(roleType.getAuthority(), roleType);
        }
    }

    public static RoleType from(@NonNull String authority) {
        return Optional
            .ofNullable(CACHE.get(authority))
            .orElseThrow(() -> new IllegalArgumentException("User's authority " + authority + "hasn't supported yet"));
    }
}
