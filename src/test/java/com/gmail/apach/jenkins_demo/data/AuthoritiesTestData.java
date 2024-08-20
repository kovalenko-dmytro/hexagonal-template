package com.gmail.apach.jenkins_demo.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthoritiesTestData {

    public static Collection<GrantedAuthority> adminAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_ADMIN"),
            new SimpleGrantedAuthority("ROLE_MANAGER"),
            new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static Collection<GrantedAuthority> managerAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_MANAGER"),
            new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static Collection<GrantedAuthority> userAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
