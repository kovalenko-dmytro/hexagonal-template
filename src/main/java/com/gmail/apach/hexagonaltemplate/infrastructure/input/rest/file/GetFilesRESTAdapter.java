package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.gmail.apach.hexagonaltemplate.application.usecase.file.GetFilesUseCase;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.FileRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Tag(name = "File REST API")
@RestController
@RequestMapping(value = "/api/v1/files")
@RequiredArgsConstructor
@Validated
public class GetFilesRESTAdapter {

    private final GetFilesUseCase getFilesUseCase;
    private final FileRESTMapper fileRESTMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<PagedModel<FileResponse>> getFiles(
        @RequestParam(required = false) String fileName,
        @RequestParam(required = false) LocalDate createdFrom,
        @RequestParam(required = false) LocalDate createdTo,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "created") String[] sort
    ) {
        final var files = getFilesUseCase.getFiles(fileName, createdFrom, createdTo, page, size, sort);
        final var response = files.map(fileRESTMapper::toFileResponse);
        return ResponseEntity.ok().body(new PagedModel<>(response));
    }
}
