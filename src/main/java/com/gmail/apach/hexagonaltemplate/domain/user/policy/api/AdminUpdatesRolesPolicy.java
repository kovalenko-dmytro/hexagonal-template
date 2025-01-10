package com.gmail.apach.hexagonaltemplate.domain.user.policy.api;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import org.springframework.lang.NonNull;

public class AdminUpdatesRolesPolicy extends AbstractPolicy<UserPermissionPolicyContext> {

    @Override
    public boolean isSatisfiedWith(@NonNull UserPermissionPolicyContext context) {
        boolean satisfied = context.principal().isAdmin()
            && !context.processed().isAdmin()
            && !context.inputAttributes().isAdmin();
        terminate(satisfied);
        return satisfied;
    }
}
