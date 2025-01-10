package com.gmail.apach.hexagonaltemplate.domain.common.policy;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import org.springframework.lang.NonNull;

public interface Policy<C> {
    boolean isSatisfiedWith(@NonNull C context);

    Policy<C> terminateIfError();

    void terminate(boolean satisfied);

    Policy<C> withErrorDetails(@NonNull DomainError domainError);

    Policy<C> withErrorDetails(@NonNull DomainError domainError, @NonNull Object[] errorArgs);

    Policy<C> and(@NonNull Policy<C> other);

    Policy<C> or(@NonNull Policy<C> other);
}
