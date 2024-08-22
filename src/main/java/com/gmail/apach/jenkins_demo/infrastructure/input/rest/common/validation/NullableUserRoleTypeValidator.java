package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.validation;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;
import java.util.Objects;

public class NullableUserRoleTypeValidator implements
    ConstraintValidator<NullableUserRoleTypeConstraint, RoleType> {

    @Override
    public void initialize(NullableUserRoleTypeConstraint contactNumber) {
    }

    @Override
    public boolean isValid(RoleType roleType, ConstraintValidatorContext cxt) {
        return Objects.isNull(roleType) || EnumSet.of(RoleType.MANAGER, RoleType.USER).contains(roleType);
    }
}

