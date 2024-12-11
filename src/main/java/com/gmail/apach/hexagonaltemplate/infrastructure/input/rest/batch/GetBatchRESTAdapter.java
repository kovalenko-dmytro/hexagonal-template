package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch;

import com.gmail.apach.hexagonaltemplate.application.port.input.batch.GetBatchInputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto.GetBatchResponse;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.BatchRESTMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Batch Monitoring API")
@RestController
@RequestMapping(value = "/api/v1/batches/{batchId}")
@RequiredArgsConstructor
@Validated
public class GetBatchRESTAdapter {

    private final GetBatchInputPort getBatchInputPort;
    private final BatchRESTMapper batchRESTMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<GetBatchResponse> getBatch(@PathVariable(value = "batchId") String batchId) {
        final var batch = getBatchInputPort.get(batchId);
        final var response = batchRESTMapper.toGetBatchResponse(batch);
        return ResponseEntity.ok().body(response);
    }
}
