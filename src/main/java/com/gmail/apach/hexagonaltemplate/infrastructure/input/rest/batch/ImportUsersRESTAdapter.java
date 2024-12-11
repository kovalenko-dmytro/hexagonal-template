package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto.ImportUsersRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@Tag(name = "Batch API")
@RestController
@RequestMapping(value = "/api/v1/batches/import-users-from-file")
@RequiredArgsConstructor
@Validated
public class ImportUsersRESTAdapter {

    private static final String BATCHES_URI_PATH = "/api/v1/batches/";

    private final ImportUsersInputPort importUsersInputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> importUsers(
        @Valid @RequestBody ImportUsersRequest request,
        HttpServletRequest httpRequest
    ) throws URISyntaxException {
        final var batchId = UUID.randomUUID().toString();
        final var username = currentPrincipalOutputPort.getPrincipal().getUsername();
        importUsersInputPort.execute(batchId, request.fileId(), username);
        return ResponseEntity.created(new URI(buildRedirectUri(httpRequest, batchId))).build();
    }

    private String buildRedirectUri(HttpServletRequest httpRequest, String batchId) {
        return httpRequest.getScheme() + "://"
            + httpRequest.getServerName() + ":"
            + httpRequest.getServerPort()
            + BATCHES_URI_PATH + batchId;
    }
}
