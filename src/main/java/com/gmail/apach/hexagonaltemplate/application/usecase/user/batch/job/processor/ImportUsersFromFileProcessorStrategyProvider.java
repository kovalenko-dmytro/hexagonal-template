package com.gmail.apach.hexagonaltemplate.application.usecase.user.batch.job.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImportUsersFromFileProcessorStrategyProvider {

    private final Map<String, ImportUsersFromFileProcessorStrategy> processors;

    public ImportUsersFromFileProcessorStrategy getProcessor(String contentType) {
        return Optional
            .ofNullable(processors.get(contentType))
            .orElseThrow(() ->
                new IllegalArgumentException("File processor for type " + contentType + "hasn't supported yet"));
    }
}
