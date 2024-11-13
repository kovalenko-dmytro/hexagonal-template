package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.GetUsersUseCase;
import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.GetUsersPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GetUsersInputPort implements GetUsersUseCase {

    private final GetUsersPermissionPolicy getUsersPermissionPolicy;
    private final GetUsersOutputPort getUsersOutputPort;

    @Override
    public Page<User> getUsers(
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
        getUsersPermissionPolicy.check();
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username(username).firstName(firstName).lastName(lastName).email(email)
            .enabled(enabled).createdFrom(createdFrom).createdTo(createdTo).createdBy(createdBy)
            .isAdmin(AuthPrincipal.getDetails().isAdmin()).page(page).size(size).sort(sort)
            .build();
        return getUsersOutputPort.getUsers(filterWrapper);
    }
}
