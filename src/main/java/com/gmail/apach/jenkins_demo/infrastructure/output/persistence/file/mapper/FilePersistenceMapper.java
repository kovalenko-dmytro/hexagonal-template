package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.mapper;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.entity.FileEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface FilePersistenceMapper {

    FileEntity toFileEntity(StoredFile file);
    StoredFile toStoredFile(FileEntity fileEntity);
}
