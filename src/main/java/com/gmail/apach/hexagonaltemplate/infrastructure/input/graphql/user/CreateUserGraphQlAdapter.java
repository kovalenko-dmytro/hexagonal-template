package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.CreateUserInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.UserGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.CreateUserInputType;
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
public class CreateUserGraphQlAdapter {

    private final CreateUserInputPort createUserInputPort;
    private final UserGraphQlMapper userGraphQlMapper;

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public UserOutputType createUser(@Argument(value = "inputType") @Valid CreateUserInputType inputType) {
        final var requestedUser = userGraphQlMapper.toUser(inputType);
        final var savedUser = createUserInputPort.createUser(requestedUser);
        return userGraphQlMapper.toUserOutputType(savedUser);
    }
}
