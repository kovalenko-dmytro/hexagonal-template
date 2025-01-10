package com.gmail.apach.hexagonaltemplate.domain.user.policy.importing;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class PasswordPolicy extends AbstractPolicy<User> {

    @Override
    public boolean isSatisfiedWith(@NonNull User user) {
        final var password = user.getPassword();

        boolean satisfied = StringUtils.isNoneBlank(password)
            && password.length() >= 4
            && password.length() <= 255;

        terminate(satisfied);
        return satisfied;
    }
}
