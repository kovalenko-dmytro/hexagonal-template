package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchInstanceView {

    private Integer jobInstanceId;
    private String jobName;
}
