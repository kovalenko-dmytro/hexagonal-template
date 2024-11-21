package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.GetFilesInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper.FileGraphQlMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.wrapper.PageOutputType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file.dto.FileOutputType;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Validated
public class GetFilesGraphQlAdapter {

    private final GetFilesInputPort getFilesInputPort;
    private final FileGraphQlMapper fileGraphQlMapper;

    @QueryMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public PageOutputType<FileOutputType> getFiles(
        @Argument(value = "fileName") String fileName,
        @Argument(value = "createdFrom") LocalDate createdFrom,
        @Argument(value = "createdTo") LocalDate createdTo,
        @Argument(value = "page") int page,
        @Argument(value = "size") int size,
        @Argument(value = "sort") List<String> sort
    ) {
        final var files = getFilesInputPort.getFiles(
            fileName, createdFrom, createdTo, page, size, sort.toArray(String[]::new));
        final var filesPage = files.map(fileGraphQlMapper::toFileOutputType);

        return new PageOutputType<>(filesPage.getContent(), filesPage.getSize(), filesPage.getNumber(),
            filesPage.getTotalElements(), filesPage.getTotalPages());
    }
}
