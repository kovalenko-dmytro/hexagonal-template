package com.gmail.apach.hexagonaltemplate.domain.batch.model;

import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchInstance;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchStatus;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchStep;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.ExitCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ExecutedBatch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String batchId;
    private Integer jobExecutionId;
    private Integer version;
    private BatchInstance batchInstance;
    private List<BatchStep> steps;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BatchStatus status;
    private ExitCode exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
}
