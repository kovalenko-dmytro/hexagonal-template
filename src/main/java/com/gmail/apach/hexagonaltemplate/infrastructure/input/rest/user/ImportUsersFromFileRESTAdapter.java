package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersFromFileInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.ImportUsersFromFileRequest;
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

@Tag(name = "Batch API")
@RestController
@RequestMapping(value = "/api/v1/batches/import-users-from-file")
@RequiredArgsConstructor
@Validated
public class ImportUsersFromFileRESTAdapter {

    private static final String REDIRECT_URI_BASE_PATH = "/api/v1/batches/";

    private final ImportUsersFromFileInputPort importUsersFromFileInputPort;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> importUsers(
        @Valid @RequestBody ImportUsersFromFileRequest request,
        HttpServletRequest httpRequest
    ) throws URISyntaxException {
        final var executedBatch = importUsersFromFileInputPort.execute(request.fileId());
        return ResponseEntity.created(new URI(buildRedirectUri(httpRequest, executedBatch.getBatchId()))).build();
    }

    private String buildRedirectUri(HttpServletRequest httpRequest, String batchId) {
        return httpRequest.getScheme() + "://"
            + httpRequest.getServerName() + ":"
            + httpRequest.getServerPort()
            + REDIRECT_URI_BASE_PATH + batchId;
    }
}
