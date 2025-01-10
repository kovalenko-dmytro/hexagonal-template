package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository.rowmapper;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view.BatchInstanceView;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view.BatchStepView;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view.BatchView;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.common.converter.JdbcColumnConverter;
import lombok.Getter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetBatchViewRowMapper implements RowMapper<BatchView> {

    @Getter
    private final Map<String, BatchView> collector = new HashMap<>();

    @Override
    public BatchView mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
        final var batchId = rs.getString("batch_id");
        var batchView = collector.get(batchId);

        if (Objects.isNull(batchView)) {
            batchView = mapRowToGetBatchView(rs);
            collector.put(batchId, batchView);
        }

        batchView.getSteps().add(mapRowToStepView(rs));

        return null;
    }

    private BatchView mapRowToGetBatchView(ResultSet rs) throws SQLException {
        return BatchView.builder()
            .batchId(rs.getString("batch_id"))
            .jobExecutionId(rs.getInt("job_execution_id"))
            .version(rs.getInt("batch_job_execution_version"))
            .batchInstance(mapRowToBatchInstanceView(rs))
            .steps(new ArrayList<>())
            .createTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_job_execution_create_time"))
            .startTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_job_execution_start_time"))
            .endTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_job_execution_end_time"))
            .status(rs.getString("batch_job_execution_status"))
            .exitCode(rs.getString("batch_job_execution_exit_code"))
            .exitMessage(rs.getString("batch_job_execution_exit_message"))
            .lastUpdated(JdbcColumnConverter.toLocalDateTime(rs, "batch_job_execution_last_updated"))
            .build();
    }

    private BatchInstanceView mapRowToBatchInstanceView(ResultSet rs) throws SQLException {
        return BatchInstanceView.builder()
            .jobInstanceId(rs.getInt("job_instance_id"))
            .jobName(rs.getString("job_name"))
            .build();
    }

    private BatchStepView mapRowToStepView(ResultSet rs) throws SQLException {
        return BatchStepView.builder()
            .jobExecutionId(rs.getInt("job_execution_id"))
            .version(rs.getInt("batch_step_execution_version"))
            .stepName(rs.getString("step_name"))
            .createTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_step_execution_create_time"))
            .startTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_step_execution_start_time"))
            .endTime(JdbcColumnConverter.toLocalDateTime(rs, "batch_step_execution_end_time"))
            .status(rs.getString("batch_step_execution_status"))
            .exitCode(rs.getString("batch_step_execution_exit_code"))
            .exitMessage(rs.getString("batch_step_execution_exit_message"))
            .lastUpdated(JdbcColumnConverter.toLocalDateTime(rs, "batch_step_execution_last_updated"))
            .build();
    }
}
