package com.gmail.apach.jenkins_demo.data;

import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CreateUserTestData {

    public static User user() {
        return User.builder()
            .username("username")
            .password("password")
            .email("email@email.com")
            .firstName("firstName")
            .lastName("lastName")
            .roles(Set.of(Role.builder().role(RoleType.USER).build()))
            .build();
    }

    public static User userWithNoRoles() {
        return User.builder()
            .username("username")
            .password("password")
            .email("email@email.com")
            .firstName("firstName")
            .lastName("lastName")
            .build();
    }

    public static User savedUser() {
        return User.builder()
            .userId("6a8d68c8-2f28-4b53-ac5a-2db586512438")
            .username("username")
            .password("encodedPassword")
            .email("email@email.com")
            .firstName("firstName")
            .lastName("lastName")
            .roles(Set.of(
                Role.builder()
                    .roleId("6a8d68c8-2f28-4b53-ac5a-2db586512437")
                    .role(RoleType.USER)
                    .build()))
            .build();
    }

    public static SendEmailWrapper emailData() {
        return SendEmailWrapper.builder()
            .sendBy("admin")
            .sendTo("email@email.com")
            .subject("Hello")
            .properties(Map.of(
                EmailType.Property.RECIPIENT_NAME.getProperty(), "username",
                EmailType.Property.SENDER_NAME.getProperty(), "admin"
            ))
            .emailType(EmailType.INVITE)
            .build();
    }
}
