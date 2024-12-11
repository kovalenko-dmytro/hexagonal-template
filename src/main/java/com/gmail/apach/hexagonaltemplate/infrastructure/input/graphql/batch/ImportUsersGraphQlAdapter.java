package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.batch;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.batch.dto.ImportUsersInputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.batch.dto.ImportUsersOutputType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Validated
public class ImportUsersGraphQlAdapter {

    private final ImportUsersInputPort importUsersInputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ImportUsersOutputType importUsers(@Argument(value = "inputType") @Valid ImportUsersInputType inputType) {
        final var jobId = UUID.randomUUID().toString();
        final var username = currentPrincipalOutputPort.getPrincipal().getUsername();
        importUsersInputPort.execute(jobId, inputType.fileId(), username);
        return new ImportUsersOutputType(jobId);
    }
}
