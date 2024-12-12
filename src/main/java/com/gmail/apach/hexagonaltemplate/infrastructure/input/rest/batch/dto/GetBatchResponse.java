package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto;

import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchStatus;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.ExitCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record GetBatchResponse(
    String batchId,
    Integer jobExecutionId,
    Integer version,
    BatchInstanceResponse batchInstance,
    List<BatchStepResponse> steps,
    LocalDateTime createTime,
    LocalDateTime startTime,
    LocalDateTime endTime,
    BatchStatus status,
    ExitCode exitCode,
    String exitMessage,
    LocalDateTime lastUpdated
) {
}