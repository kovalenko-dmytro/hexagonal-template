package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.mapper;

import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.BatchStatus;
import com.gmail.apach.hexagonaltemplate.domain.batch.vo.ExitCode;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.view.BatchView;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BatchDbMapper {

    ExecutedBatch toExecutedBatch(BatchView view);

    default BatchStatus toBatchStatus(String status) {
        return BatchStatus.from(status);
    }

    default ExitCode toExitCode(String exitCode) {
        return ExitCode.from(exitCode);
    }
}
