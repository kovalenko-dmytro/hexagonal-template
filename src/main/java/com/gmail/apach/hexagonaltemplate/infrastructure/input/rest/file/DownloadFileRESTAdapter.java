package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.usecase.file.DownloadFileUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "File REST API")
@RestController
@RequestMapping(value = "/api/v1/files/{fileId}")
@RequiredArgsConstructor
@Validated
public class DownloadFileRESTAdapter {

    private final DownloadFileUseCase downloadFileUseCase;

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") String fileId) {
        final var file = downloadFileUseCase.downloadFile(fileId);
        final var headerValues = "attachment; filename=\"" + file.getFileName() + "\"";
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
            .body(file.getPayload());
    }
}
