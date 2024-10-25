package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.usecase.file.DeleteFileUseCase;
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

    private final DeleteFileUseCase deleteFileUseCase;

    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<Void> deleteFile(@PathVariable(value = "fileId") String fileId) {
        deleteFileUseCase.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }
}
