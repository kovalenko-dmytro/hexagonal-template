package com.gmail.apach.hexagonaltemplate.domain.common.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.operator.AndPolicy;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.operator.OrPolicy;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.util.Objects;

@NoArgsConstructor
public abstract class AbstractPolicy<C> implements Policy<C> {

    protected DomainError domainError;
    protected Object[] errorArgs;
    private boolean isTerminated;

    public abstract boolean isSatisfiedWith(@NonNull C context);

    @Override
    public Policy<C> withErrorDetails(@NonNull DomainError domainError) {
        this.domainError = domainError;
        return this;
    }

    @Override
    public Policy<C> withErrorDetails(@NonNull DomainError domainError, @NonNull Object[] errorArgs) {
        this.withErrorDetails(domainError);
        this.errorArgs = errorArgs;
        return this;
    }

    @Override
    public Policy<C> terminateIfError() {
        Objects.requireNonNull(domainError, "domain error type should be set up");
        this.isTerminated = true;
        return this;
    }

    @Override
    public void terminate(boolean satisfied) {
        if (!satisfied && isTerminated) {
            throw new PolicyViolationException(domainError, errorArgs);
        }
    }

    @Override
    public Policy<C> and(@NonNull Policy<C> other) {
        return new AndPolicy<>(this, other);
    }

    @Override
    public Policy<C> or(@NonNull Policy<C> other) {
        return new OrPolicy<>(this, other);
    }
}
