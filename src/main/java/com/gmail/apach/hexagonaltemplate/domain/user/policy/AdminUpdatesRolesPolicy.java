package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserValidationContext;
import org.springframework.lang.NonNull;

public class AdminUpdatesRolesPolicy extends AbstractPolicy<UserValidationContext> {

    @Override
    public boolean isSatisfiedWith(@NonNull UserValidationContext context) {
        boolean satisfied = context.principal().isAdmin()
            && !context.processed().isAdmin()
            && !context.inputAttributes().isAdmin();
        terminate(satisfied);
        return satisfied;
    }
}
