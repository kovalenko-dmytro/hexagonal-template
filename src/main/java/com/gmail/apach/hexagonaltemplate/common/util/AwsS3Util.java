package com.gmail.apach.hexagonaltemplate.common.util;

import com.gmail.apach.hexagonaltemplate.common.config.s3.AwsS3BucketProperties;
import com.gmail.apach.hexagonaltemplate.common.constant.message.Error;
import com.gmail.apach.hexagonaltemplate.common.exception.ApplicationServerException;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AwsS3Util {

    private final S3Template s3Template;
    private final AwsS3BucketProperties awsS3BucketProperties;
    private final MessageSource messageSource;

    public S3Resource save(@NotNull MultipartFile file) {
        final var objectKey = UUID.randomUUID().toString();
        try {
            return s3Template.upload(awsS3BucketProperties.getBucketName(), objectKey, file.getInputStream());
        } catch (IOException e) {
            final var args = new Object[]{file.getOriginalFilename(), e.getMessage()};
            throw new ApplicationServerException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_UNABLE_UPLOAD.getKey(), args, LocaleContextHolder.getLocale()));
        }
    }

    public S3Resource get(@NotNull String objectKey) {
        return s3Template.download(awsS3BucketProperties.getBucketName(), objectKey);
    }

    public void delete(@NotNull String objectKey) {
        s3Template.deleteObject(awsS3BucketProperties.getBucketName(), objectKey);
    }
}
