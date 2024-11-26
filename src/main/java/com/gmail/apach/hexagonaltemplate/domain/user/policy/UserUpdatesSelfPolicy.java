package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;

public class UserUpdatesSelfPolicy extends AbstractPolicy<UserPermissionPolicyContext> {

    @Override
    public boolean isSatisfiedWith(UserPermissionPolicyContext context) {
        return context.principal().isUser() && context.processed().isSelf(context.principal());
    }
}