package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFilesOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.file.GetFilesUseCase;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.wrapper.GetFilesFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GetFilesInputPort implements GetFilesUseCase {

    private final GetFilesOutputPort getFilesOutputPort;

    @Override
    public Page<StoredFile> getFiles(String fileName,
                                     LocalDate createdFrom,
                                     LocalDate createdTo,
                                     int page,
                                     int size,
                                     String[] sort
    ) {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .fileName(fileName).createdFrom(createdFrom).createdTo(createdTo).page(page).size(size).sort(sort)
            .build();
        return getFilesOutputPort.getFiles(filterWrapper);
    }
}
