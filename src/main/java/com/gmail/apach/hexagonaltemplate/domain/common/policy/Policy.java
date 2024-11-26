package com.gmail.apach.hexagonaltemplate.domain.common.policy;

public interface Policy<C> {
    boolean isSatisfiedWith(C context);
    Policy<C> and(Policy<C> other);
    Policy<C> or(Policy<C> other);
}
