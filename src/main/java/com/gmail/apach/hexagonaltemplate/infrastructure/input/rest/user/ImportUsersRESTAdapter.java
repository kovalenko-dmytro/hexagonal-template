package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.ImportUsersRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.ImportUsersResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Batch API")
@RestController
@RequestMapping(value = "/api/v1/batch/import-users-from-file")
@RequiredArgsConstructor
@Validated
public class ImportUsersRESTAdapter {

    private final ImportUsersInputPort importUsersInputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ImportUsersResponse> importUsers(@Valid @RequestBody ImportUsersRequest request) {
        final var jobId = UUID.randomUUID().toString();
        final var username = currentPrincipalOutputPort.getPrincipal().getUsername();
        importUsersInputPort.execute(jobId, request.fileId(), username);
        //TODO redirect to batch get status API and add async
        return ResponseEntity.status(HttpStatus.CREATED).body(new ImportUsersResponse(jobId));
    }
}
