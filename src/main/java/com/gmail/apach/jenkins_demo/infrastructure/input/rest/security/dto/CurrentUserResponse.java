package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record CurrentUserResponse(
    String userId,
    String username,
    String firstName,
    String lastName,
    String email,
    boolean enabled,
    LocalDateTime created,
    String createdBy,
    Set<RoleType> roles
) {}
