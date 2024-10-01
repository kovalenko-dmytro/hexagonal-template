package com.gmail.apach.hexagonaltemplate.data;

import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RolesTestData {

    public static Set<Role> adminRoles() {
        return Set.of(
            Role.builder().role(RoleType.ADMIN).build(),
            Role.builder().role(RoleType.MANAGER).build(),
            Role.builder().role(RoleType.USER).build());
    }

    public static Set<Role> managerRoles() {
        return Set.of(Role.builder().role(RoleType.MANAGER).build(), Role.builder().role(RoleType.USER).build());
    }

    public static Set<Role> userRoles() {
        return Set.of(Role.builder().role(RoleType.USER).build());
    }
}
