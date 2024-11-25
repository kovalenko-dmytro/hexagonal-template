package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileRESTMapper {
    @Mapping(target = "storageKey", source = "file.storedResource.storageKey")
    FileResponse toFileResponse(StoredFile file);
}
