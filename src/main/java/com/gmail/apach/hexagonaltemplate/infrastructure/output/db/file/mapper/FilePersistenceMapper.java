package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.mapper;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.entity.FileEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FilePersistenceMapper {
    FileEntity toFileEntity(StoredFile file);
    StoredFile toStoredFile(FileEntity fileEntity);
}
