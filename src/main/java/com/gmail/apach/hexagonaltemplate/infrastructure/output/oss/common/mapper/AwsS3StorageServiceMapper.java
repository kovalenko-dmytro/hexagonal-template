package com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.common.mapper;

import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import io.awspring.cloud.s3.S3Resource;
import org.mapstruct.*;

import java.io.IOException;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AwsS3StorageServiceMapper {

    @Mapping(target = "storageKey", source = "resource.location.object")
    @Mapping(target = "payload", source = "resource", qualifiedByName = "getPayload")
    StoredResource toStoredResource(S3Resource resource);

    @Named("getPayload")
    default byte[] getPayload(S3Resource resource) throws IOException {
        return resource.getInputStream().readAllBytes();
    }
}
