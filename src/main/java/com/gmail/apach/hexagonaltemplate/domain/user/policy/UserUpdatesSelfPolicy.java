package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserValidationContext;
import org.springframework.lang.NonNull;

public class UserUpdatesSelfPolicy extends AbstractPolicy<UserValidationContext> {

    @Override
    public boolean isSatisfiedWith(@NonNull UserValidationContext context) {
        return context.principal().isUser() && context.processed().isSelf(context.principal());
    }
}
