package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImportUsersFromFileProcessorProvider {

    private final Map<String, ImportUsersFromFileProcessor> processors;

    public ImportUsersFromFileProcessor getProcessor(String contentType) {
        return Optional
            .ofNullable(processors.get(contentType))
            .orElseThrow(() ->
                new IllegalArgumentException("File processor for type " + contentType + "hasn't supported yet"));
    }
}
