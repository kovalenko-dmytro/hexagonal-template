package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileCacheConstant {

    public static final String CACHE_NAME = "file";
    public static final String LIST_CACHE_NAME = "files";

    public static class Key {

        public static final String ID = "#fileId";
        public static final String SEARCH = "{#wrapper.page, #wrapper.size}";

    }

    public static class Condition {

        public static final String SEARCH = """ 
            {
                #wrapper.fileName == null             
                && #wrapper.createdFrom == null
                && #wrapper.createdTo == null                                
                && #wrapper.sort != null
                && #wrapper.sort.length == 1
                && #wrapper.sort[0].contentEquals("created")
            }
            """;
    }
}
