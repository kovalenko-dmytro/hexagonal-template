package com.gmail.apach.hexagonaltemplate.domain.common.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.operator.AndPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.operator.OrPolicy;

public abstract class AbstractPolicy<C> implements Policy<C> {

    public abstract boolean isSatisfiedWith(C context);

    @Override
    public Policy<C> and(Policy<C> other) {
        return new AndPolicy<>(this, other);
    }

    @Override
    public Policy<C> or(Policy<C> other) {
        return new OrPolicy<>(this, other);
    }
}
