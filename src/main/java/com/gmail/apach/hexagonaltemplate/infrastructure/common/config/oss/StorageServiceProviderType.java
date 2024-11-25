package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.oss;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StorageServiceProviderType {

    public static final String S3 = "S3";
    public static final String COS = "COS";
    public static final String GCS = "GCS";
}
