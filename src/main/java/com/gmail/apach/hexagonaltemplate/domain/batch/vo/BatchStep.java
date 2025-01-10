package com.gmail.apach.hexagonaltemplate.domain.batch.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchStep implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private Integer jobExecutionId;
    private Integer version;
    private String stepName;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BatchStatus status;
    private ExitCode exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
}
