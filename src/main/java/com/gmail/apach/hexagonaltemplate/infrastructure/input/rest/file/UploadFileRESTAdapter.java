package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.UploadFileInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.FileRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File REST API")
@RestController
@RequestMapping(value = "/api/v1/files")
@RequiredArgsConstructor
@Validated
public class UploadFileRESTAdapter {

    private final UploadFileInputPort uploadFileInputPort;
    private final FileRESTMapper fileRESTMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<FileResponse> uploadFile(@RequestParam(value = "file") MultipartFile file) {
        final var storedFile = uploadFileInputPort.upload(file);
        final var response = fileRESTMapper.toFileResponse(storedFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
