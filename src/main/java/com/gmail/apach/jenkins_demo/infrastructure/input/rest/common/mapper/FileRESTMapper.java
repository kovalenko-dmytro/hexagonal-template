package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.mapper;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.file.dto.FileResponse;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface FileRESTMapper {

    FileResponse toFileResponse(StoredFile file);
}
