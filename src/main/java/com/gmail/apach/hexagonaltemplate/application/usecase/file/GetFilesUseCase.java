package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.input.file.GetFilesInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFilesOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class GetFilesUseCase implements GetFilesInputPort {

    private final GetFilesOutputPort getFilesOutputPort;

    @Override
    public Page<StoredFile> get(
        String fileName,
        LocalDate createdFrom,
        LocalDate createdTo,
        int page,
        int size,
        String[] sort
    ) {
        final var filterWrapper = GetFilesFilterWrapper.builder()
            .fileName(fileName).createdFrom(createdFrom).createdTo(createdTo).page(page).size(size).sort(sort)
            .build();
        return getFilesOutputPort.get(filterWrapper);
    }
}
