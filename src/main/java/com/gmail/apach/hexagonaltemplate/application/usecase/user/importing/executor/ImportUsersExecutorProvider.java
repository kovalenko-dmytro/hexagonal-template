package com.gmail.apach.hexagonaltemplate.application.usecase.user.importing.executor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ImportUsersExecutorProvider {

    private final Map<String, ImportUsersExecutor> executors;

    public ImportUsersExecutor getExecutor(String contentType) {
        return Optional
            .ofNullable(executors.get(contentType))
            .orElseThrow(() ->
                new IllegalArgumentException("Batch executor for content type " + contentType + "hasn't supported yet"));
    }
}
