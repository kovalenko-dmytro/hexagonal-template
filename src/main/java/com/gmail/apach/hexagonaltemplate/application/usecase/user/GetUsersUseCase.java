package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.GetUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.UserPermissionPolicyService;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GetUsersUseCase implements GetUsersInputPort {

    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;
    private final GetUsersOutputPort getUsersOutputPort;

    @Override
    public Page<User> get(
        String username,
        String firstName,
        String lastName,
        String email,
        Boolean enabled,
        LocalDate createdFrom,
        LocalDate createdTo,
        String createdBy,
        int page,
        int size,
        String[] sort
    ) {
        final var principal = currentPrincipalOutputPort.getPrincipal();
        UserPermissionPolicyService.checkGetUsersPolicy(preparePolicyContext(principal));

        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username(username).firstName(firstName).lastName(lastName).email(email)
            .enabled(enabled).createdFrom(createdFrom).createdTo(createdTo).createdBy(createdBy)
            .isAdmin(principal.isAdmin()).page(page).size(size).sort(sort)
            .build();

        return getUsersOutputPort.get(filterWrapper);
    }

    private UserPermissionPolicyContext preparePolicyContext(User principal) {
        return UserPermissionPolicyContext.builder()
            .principal(principal)
            .build();
    }
}
