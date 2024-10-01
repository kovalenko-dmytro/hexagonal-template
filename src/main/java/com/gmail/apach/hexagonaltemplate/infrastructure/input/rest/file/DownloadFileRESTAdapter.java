package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.input.file.DownloadFileInputPort;
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

    private final DownloadFileInputPort downloadFileInputPort;

    @GetMapping
    @ResponseBody
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("fileId") String fileId) {
        final var filePayload = downloadFileInputPort.downloadFile(fileId);
        final var headerValues = "attachment; filename=\"" + filePayload.fileName() + "\"";
        return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, headerValues)
            .body(filePayload.payload());
    }
}
