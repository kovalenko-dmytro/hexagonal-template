package com.gmail.apach.hexagonaltemplate.domain.user.policy.importing;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

import java.util.regex.Pattern;

@NoArgsConstructor
public class EmailPolicy extends AbstractPolicy<User> {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    @Override
    public boolean isSatisfiedWith(@NonNull User user) {
        final var email = user.getEmail();

        boolean satisfied = StringUtils.isNoneBlank(email)
            && email.length() <= 255
            && EMAIL_PATTERN.matcher(email).find();

        terminate(satisfied);
        return satisfied;
    }
}
