package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.validation.NullableUserRoleTypeConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserInputType(
    @Size(min = 2, max = 50)
    String firstName,

    @Size(min = 2, max = 50)
    String lastName,

    @Size(max = 255)
    @Email
    String email,

    Boolean enabled,

    @NullableUserRoleTypeConstraint
    RoleType roleType
) {}
