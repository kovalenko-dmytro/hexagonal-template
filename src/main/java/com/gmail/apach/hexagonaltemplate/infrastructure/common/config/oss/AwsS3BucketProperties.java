package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aws.s3")
@Getter
@Setter
public class AwsS3BucketProperties {

    @NotBlank
    private String bucketName;
}
