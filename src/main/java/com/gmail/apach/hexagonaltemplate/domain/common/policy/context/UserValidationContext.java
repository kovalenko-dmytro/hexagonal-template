package com.gmail.apach.hexagonaltemplate.domain.common.policy.context;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.Builder;

@Builder
public record UserValidationContext(
    User processed,
    User inputAttributes,
    User principal
) {
}
