package com.gmail.apach.jenkins_demo.data;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UsersTestData {

    public static User admin() {
        return User.builder()
            .username("admin")
            .password("password")
            .firstName("admin")
            .lastName("admin")
            .email("admin@admin.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("admin")
            .roles(Set.of(
                Role.builder().role(RoleType.ADMIN).build(),
                Role.builder().role(RoleType.MANAGER).build(),
                Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User manager() {
        return User.builder()
            .username("manager")
            .password("password")
            .firstName("manager")
            .lastName("manager")
            .email("manager@manager.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("admin")
            .roles(Set.of(Role.builder().role(RoleType.MANAGER).build(), Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User anotherManager() {
        return User.builder()
            .username("another_manager")
            .password("password")
            .firstName("manager")
            .lastName("manager")
            .email("manager@manager.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("admin")
            .roles(Set.of(Role.builder().role(RoleType.MANAGER).build(), Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User userCreatedByAdmin() {
        return User.builder()
            .username("userCreatedByAdmin")
            .password("password")
            .firstName("user")
            .lastName("user")
            .email("user@user.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("admin")
            .roles(Set.of(Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User userCreatedByManager() {
        return User.builder()
            .username("userCreatedByManager")
            .password("password")
            .firstName("user")
            .lastName("user")
            .email("user@user.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("manager")
            .roles(Set.of(Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User userCreatedByAnotherManager() {
        return User.builder()
            .username("userCreatedByAnotherManager")
            .password("password")
            .firstName("user")
            .lastName("user")
            .email("user@user.com")
            .enabled(true)
            .created(LocalDateTime.now())
            .createdBy("another_manager")
            .roles(Set.of(Role.builder().role(RoleType.USER).build()))
            .build();
    }
}
