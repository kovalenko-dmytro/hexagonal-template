package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.mapper;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.entity.FileEntity;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileDbMapper {
    @Mapping(target = "storageKey", source = "file.storedResource.storageKey")
    FileEntity toFileEntity(StoredFile file);
    @Mapping(target = "storedResource.storageKey", source = "storageKey")
    StoredFile toStoredFile(FileEntity fileEntity);
}
