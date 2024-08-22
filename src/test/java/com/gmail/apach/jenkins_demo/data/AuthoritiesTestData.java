package com.gmail.apach.jenkins_demo.data;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthoritiesTestData {

    public static Collection<String> adminAuthorities() {
        return List.of(RoleType.ADMIN.getAuthority(), RoleType.MANAGER.getAuthority(), RoleType.USER.getAuthority());
    }

    public static Collection<String> managerAuthorities() {
        return List.of(RoleType.MANAGER.getAuthority(), RoleType.USER.getAuthority());
    }

    public static Collection<String> userAuthorities() {
        return List.of(RoleType.USER.getAuthority());
    }
}
