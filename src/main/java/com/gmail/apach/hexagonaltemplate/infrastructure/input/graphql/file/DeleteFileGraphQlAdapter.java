package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.DeleteFileInputPort;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Controller
@RequiredArgsConstructor
@Validated
public class DeleteFileGraphQlAdapter {

    private final DeleteFileInputPort deleteFileInputPort;

    @MutationMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void deleteByFileId(@Argument(value = "fileId") String fileId) {
        deleteFileInputPort.deleteByFileId(fileId);
    }
}
