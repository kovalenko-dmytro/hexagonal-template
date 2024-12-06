package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.ImportUsersInputType;
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
public class ImportUsersGraphQlAdapter {

    private final ImportUsersInputPort importUsersInputPort;

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void importUsers(@Argument(value = "inputType") @Valid ImportUsersInputType inputType) {
        importUsersInputPort.execute(inputType.fileId());
    }
}
