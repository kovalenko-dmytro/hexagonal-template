package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto;

import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record UserResponse(
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
