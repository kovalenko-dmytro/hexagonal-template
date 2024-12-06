package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.ImportUsersInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.ImportUsersRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
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

@Tag(name = "User Batch API")
@RestController
@RequestMapping(value = "/api/v1/batch/users/import-from-file")
@RequiredArgsConstructor
@Validated
public class ImportUsersRESTAdapter {

    private final ImportUsersInputPort importUsersInputPort;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<UserResponse> importUsers(@Valid @RequestBody ImportUsersRequest request) {
        importUsersInputPort.execute(request.fileId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
