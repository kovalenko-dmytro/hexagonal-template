package com.gmail.apach.jenkins_demo.domain.file.wrapper;

import lombok.Builder;

@Builder
public record FilePayloadWrapper(
    String fileName,
    byte[] payload
) {
}
