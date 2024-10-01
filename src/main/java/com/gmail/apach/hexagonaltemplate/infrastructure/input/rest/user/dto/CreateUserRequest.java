package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto;

import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.validation.CreateUserRoleTypeConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateUserRequest(
    @NotBlank
    @Size(min = 2, max = 255)
    String username,
    @NotBlank
    @Size(min = 4, max = 255)
    String password,
    @NotBlank
    @Size(min = 2, max = 50)
    String firstName,
    @NotBlank
    @Size(min = 2, max = 50)
    String lastName,
    @NotBlank
    @Size(max = 255)
    @Email
    String email,
    @NotNull
    @CreateUserRoleTypeConstraint
    RoleType roleType
) {}
