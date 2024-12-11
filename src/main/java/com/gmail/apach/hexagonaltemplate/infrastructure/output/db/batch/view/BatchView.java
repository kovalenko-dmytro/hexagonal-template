package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchView {

    private String batchId;
    private Integer jobExecutionId;
    private Integer version;
    private BatchInstanceView batchInstance;
    private List<BatchStepView> steps;
    private LocalDateTime createTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String exitCode;
    private String exitMessage;
    private LocalDateTime lastUpdated;
}
