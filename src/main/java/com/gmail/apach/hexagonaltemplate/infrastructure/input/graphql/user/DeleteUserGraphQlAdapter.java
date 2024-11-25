package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.DeleteUserInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class DeleteUserGraphQlAdapter {

    private final DeleteUserInputPort deleteUserInputPort;

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteByUserId(@Argument(value = "userId") String userId) {
        deleteUserInputPort.deleteByUserId(userId);
    }
}
