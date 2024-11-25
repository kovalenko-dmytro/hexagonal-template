package com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.s3;

import com.gmail.apach.hexagonaltemplate.application.port.output.oss.ObjectStorageServiceOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.FileProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.StorageServiceProviderType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss.s3.AwsS3BucketProperties;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.common.mapper.AwsS3StorageServiceMapper;
import io.awspring.cloud.s3.S3Template;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component(StorageServiceProviderType.S3)
@RequiredArgsConstructor
public class AwsS3StorageServiceAdapter implements ObjectStorageServiceOutputPort {

    private final S3Template s3Template;
    private final AwsS3BucketProperties awsS3BucketProperties;
    private final AwsS3StorageServiceMapper awsS3StorageServiceMapper;
    private final MessageSource messageSource;

    @Override
    public StoredResource save(@NotNull MultipartFile file) {
        final var objectKey = UUID.randomUUID().toString();
        try {
            final var resource =
                s3Template.upload(awsS3BucketProperties.getBucketName(), objectKey, file.getInputStream());
            return awsS3StorageServiceMapper.toStoredResource(resource);
        } catch (IOException e) {
            final var args = new Object[]{file.getOriginalFilename(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_UPLOAD.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }

    @Override
    public StoredResource get(@NotNull String objectKey) {
        final var resource = s3Template.download(awsS3BucketProperties.getBucketName(), objectKey);
        return awsS3StorageServiceMapper.toStoredResource(resource);
    }

    @Override
    @RabbitListener(queues = FileProcessingConfig.DELETE_FILE_QUEUE)
    public void delete(@NotNull String objectKey) {
        s3Template.deleteObject(awsS3BucketProperties.getBucketName(), objectKey);
    }
}
