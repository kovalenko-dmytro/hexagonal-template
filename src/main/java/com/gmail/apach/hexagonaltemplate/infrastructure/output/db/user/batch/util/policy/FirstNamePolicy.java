package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class FirstNamePolicy extends AbstractPolicy<User> {

    @Override
    public boolean isSatisfiedWith(@NonNull User user) {
        final var firstName = user.getFirstName();

        boolean satisfied = StringUtils.isNoneBlank(firstName)
            && firstName.length() >= 2
            && firstName.length() <= 50;

        terminate(satisfied);
        return satisfied;
    }
}
