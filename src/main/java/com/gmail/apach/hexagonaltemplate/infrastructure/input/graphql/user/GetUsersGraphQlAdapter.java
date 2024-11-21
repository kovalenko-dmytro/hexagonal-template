package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.GetUsersInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.UserGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class GetUsersGraphQlAdapter {

    private final GetUsersInputPort getUsersInputPort;
    private final UserGraphQlMapper userGraphQlMapper;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PageOutputType<UserOutputType> getUsers(
        @Argument(value = "username") String username,
        @Argument(value = "firstName") String firstName,
        @Argument(value = "lastName") String lastName,
        @Argument(value = "email") String email,
        @Argument(value = "enabled") Boolean enabled,
        @Argument(value = "createdFrom") LocalDate createdFrom,
        @Argument(value = "createdTo") LocalDate createdTo,
        @Argument(value = "createdBy") String createdBy,
        @Argument(value = "page") int page,
        @Argument(value = "size") int size,
        @Argument(value = "sort") List<String> sort
    ) {
        final var users = getUsersInputPort.getUsers(username, firstName, lastName, email, enabled,
            createdFrom, createdTo, createdBy, page, size, sort.toArray(String[]::new));
        final var usersPage = users.map(userGraphQlMapper::toUserOutputType);

        return new PageOutputType<>(usersPage.getContent(), usersPage.getSize(), usersPage.getNumber(),
            usersPage.getTotalElements(), usersPage.getTotalPages());
    }
}
