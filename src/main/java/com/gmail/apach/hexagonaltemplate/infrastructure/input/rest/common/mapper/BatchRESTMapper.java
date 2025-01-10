package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper;

import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto.GetExecutedBatchResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BatchRESTMapper {
    GetExecutedBatchResponse toGetBatchResponse(ExecutedBatch batch);
}
