package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CreateUserRoleTypeValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CreateUserRoleTypeConstraint {
    String message() default "Input role type should be MANAGER or USER";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
