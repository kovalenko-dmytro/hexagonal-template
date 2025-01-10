package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository.query;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GetBatchQuery {
    public static final String QUERY = """
            SELECT
                bjep.parameter_value          as batch_id,
                bjep.job_execution_id,
                bje.version                   as batch_job_execution_version,
                bje.job_instance_id,
                bje.create_time               as batch_job_execution_create_time,
                bje.start_time                as batch_job_execution_start_time,
                bje.end_time                  as batch_job_execution_end_time,
                bje.status                    as batch_job_execution_status,
                bje.exit_code                 as batch_job_execution_exit_code,
                bje.exit_message              as batch_job_execution_exit_message,
                bje.last_updated              as batch_job_execution_last_updated,
                bji.job_name,
                bse.version                   as batch_step_execution_version,
                bse.step_name,
                bse.create_time               as batch_step_execution_create_time,
                bse.start_time                as batch_step_execution_start_time,
                bse.end_time                  as batch_step_execution_end_time,
                bse.status                    as batch_step_execution_status,
                bse.exit_code                 as batch_step_execution_exit_code,
                bse.exit_message              as batch_step_execution_exit_message,
                bse.last_updated              as batch_step_execution_last_updated
            FROM
                batch_job_execution_params bjep
            LEFT JOIN
                batch_job_execution bje
                    ON bjep.job_execution_id = bje.job_execution_id
            LEFT JOIN
                batch_job_instance bji
                    ON bje.job_instance_id = bji.job_instance_id
            LEFT JOIN
                batch_step_execution bse
                    ON bjep.job_execution_id = bse.job_execution_id
            WHERE bjep.parameter_value = '%s'
        """;
}
