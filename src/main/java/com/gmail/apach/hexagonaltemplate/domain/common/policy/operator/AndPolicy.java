package com.gmail.apach.hexagonaltemplate.domain.common.policy.operator;

import com.gmail.apach.hexagonaltemplate.domain.common.policy.AbstractPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.Policy;

public class AndPolicy<C> extends AbstractPolicy<C> {

    private final Policy<C> left;
    private final Policy<C> right;

    public AndPolicy(Policy<C> left, Policy<C> right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public boolean isSatisfiedWith(C context) {
        return left.isSatisfiedWith(context) && right.isSatisfiedWith(context);
    }
}
