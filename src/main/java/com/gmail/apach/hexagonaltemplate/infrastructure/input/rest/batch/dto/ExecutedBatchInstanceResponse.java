package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto;

import lombok.Builder;

@Builder
public record ExecutedBatchInstanceResponse(
    Integer jobInstanceId,
    String jobName
) {
}
