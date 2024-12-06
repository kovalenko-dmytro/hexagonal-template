package com.gmail.apach.hexagonaltemplate.domain.user.policy.api;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import org.springframework.lang.NonNull;

public class UserUpdatesSelfPolicy extends AbstractPolicy<UserPermissionPolicyContext> {

    @Override
    public boolean isSatisfiedWith(@NonNull UserPermissionPolicyContext context) {
        return context.principal().isUser() && context.processed().isSelf(context.principal());
    }
}
