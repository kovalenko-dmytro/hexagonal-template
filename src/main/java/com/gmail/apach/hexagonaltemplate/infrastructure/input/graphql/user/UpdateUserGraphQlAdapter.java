package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.UpdateUserUseCase;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.UserGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UpdateUserInputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class UpdateUserGraphQlAdapter {

    private final UpdateUserUseCase updateUserUseCase;
    private final UserGraphQlMapper userGraphQlMapper;

    @MutationMapping
    @PreAuthorize("hasRole('USER')")
    public UserOutputType updateUser(
        @Argument(value = "userId") String userId,
        @Argument(value = "inputType") @Valid UpdateUserInputType inputType
    ) {
        final var requestedUser = userGraphQlMapper.toUser(userId, inputType);
        final var updatedUser = updateUserUseCase.update(requestedUser);
        return userGraphQlMapper.toUserOutputType(updatedUser);
    }
}
