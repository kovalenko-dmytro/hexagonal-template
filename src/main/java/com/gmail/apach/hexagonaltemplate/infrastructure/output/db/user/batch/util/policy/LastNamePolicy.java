package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.util.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class LastNamePolicy extends AbstractPolicy<User> {

    @Override
    public boolean isSatisfiedWith(@NonNull User user) {
        final var lastName = user.getLastName();

        boolean satisfied = StringUtils.isNoneBlank(lastName)
            && lastName.length() >= 2
            && lastName.length() <= 50;

        terminate(satisfied);
        return satisfied;
    }
}
