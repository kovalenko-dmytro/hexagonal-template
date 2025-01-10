package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository.query.GetBatchQuery;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository.rowmapper.GetBatchViewRowMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view.BatchView;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public Optional<BatchView> get(String batchId) {
        final var rowMapper = new GetBatchViewRowMapper();
        jdbcTemplate.query(GetBatchQuery.QUERY.formatted(batchId), rowMapper);
        final var collector = rowMapper.getCollector();
        return Optional.ofNullable(collector.get(batchId));
    }
}
