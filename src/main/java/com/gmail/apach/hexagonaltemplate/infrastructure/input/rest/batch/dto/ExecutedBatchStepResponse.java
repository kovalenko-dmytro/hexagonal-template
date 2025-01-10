package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto;

import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchStatus;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.ExitCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ExecutedBatchStepResponse(
    Integer jobExecutionId,
    Integer version,
    String stepName,
    LocalDateTime createTime,
    LocalDateTime startTime,
    LocalDateTime endTime,
    BatchStatus status,
    ExitCode exitCode,
    String exitMessage,
    LocalDateTime lastUpdated
) {
}
