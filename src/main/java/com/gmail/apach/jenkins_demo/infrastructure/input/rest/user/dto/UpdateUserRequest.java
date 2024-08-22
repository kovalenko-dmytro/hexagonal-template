package com.gmail.apach.jenkins_demo.infrastructure.input.rest.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.validation.NullableUserRoleTypeConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record UpdateUserRequest(
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
