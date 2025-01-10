package com.gmail.apach.hexagonaltemplate.domain.user.policy.importing;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.NonNull;

@NoArgsConstructor
public class RoleTypePolicy extends AbstractPolicy<User> {

    @Override
    public boolean isSatisfiedWith(@NonNull User user) {
        final var roleTypes = user.getRoleTypes();
        boolean satisfied = CollectionUtils.isNotEmpty(roleTypes) && !roleTypes.contains(RoleType.ADMIN);
        terminate(satisfied);
        return satisfied;
    }
}
