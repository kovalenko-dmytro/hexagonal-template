package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.UserGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.UserOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class GetUserGraphQlAdapter {

    private final GetUserInputPort getUserInputPort;
    private final UserGraphQlMapper userGraphQlMapper;

    @QueryMapping
    @PreAuthorize("hasRole('USER')")
    public UserOutputType getByUserId(@Argument(value = "userId") String userId) {
        final var requestedUser = getUserInputPort.getByUserId(userId);
        return userGraphQlMapper.toUserOutputType(requestedUser);
    }
}
