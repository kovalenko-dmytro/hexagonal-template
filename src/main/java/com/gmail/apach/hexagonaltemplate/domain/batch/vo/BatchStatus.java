package com.gmail.apach.hexagonaltemplate.domain.batch.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum BatchStatus {

    COMPLETED("COMPLETED"),
    STARTING("STARTING"),
    STARTED("STARTED"),
    STOPPING("STOPPING"),
    STOPPED("STOPPED"),
    FAILED("FAILED"),
    ABANDONED("ABANDONED"),
    UNKNOWN("UNKNOWN");

    private static final Map<String, BatchStatus> CACHE = new HashMap<>();
    private final String status;

    static {
        for (var batchStatus : BatchStatus.values()) {
            CACHE.put(batchStatus.status, batchStatus);
        }
    }

    public static BatchStatus from(String value) {
        return Optional
            .ofNullable(CACHE.get(value))
            .orElseThrow(() -> new IllegalArgumentException("Batch status " + value + "hasn't supported yet"));
    }
}
