package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchStepView {

    private Integer jobExecutionId;
    private Integer version;
    private String stepName;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
}
