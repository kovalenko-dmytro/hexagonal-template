package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.DeleteFileInputPort;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "File REST API")
@RestController
@RequestMapping(value = "/api/v1/files/{fileId}")
@RequiredArgsConstructor
@Validated
public class DeleteFileRESTAdapter {

    private final DeleteFileInputPort deleteFileInputPort;

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteByFileId(@PathVariable(value = "fileId") String fileId) {
        deleteFileInputPort.deleteByFileId(fileId);
        return ResponseEntity.noContent().build();
    }
}
