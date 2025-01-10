package com.gmail.apach.hexagonaltemplate.domain.common.policy.operator;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.Policy;
import org.springframework.lang.NonNull;

public class OrPolicy<C> extends AbstractPolicy<C> {

    private final Policy<C> left;
    private final Policy<C> right;

    public OrPolicy(Policy<C> left, Policy<C> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedWith(@NonNull C context) {
        final var satisfied = left.isSatisfiedWith(context) || right.isSatisfiedWith(context);
        terminate(satisfied);
        return satisfied;
    }
}
