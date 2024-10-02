package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailCacheConstant {

    public static final String CACHE_NAME = "email";
    public static final String LIST_CACHE_NAME = "emails";

    public static class Key {

        public static final String ID = "#emailId";
        public static final String EMAIL__ID = "#email.emailId";
        public static final String SEARCH = "{#wrapper.page, #wrapper.size}";

    }

    public static class Condition {

        public static final String SEARCH = """ 
                {
                    #wrapper.sendBy == null
                    && #wrapper.sendTo == null
                    && #wrapper.dateSendFrom == null
                    && #wrapper.dateSendTo == null
                    && #wrapper.emailType == null
                    && #wrapper.emailStatus == null                    
                    && #wrapper.sort != null
                    && #wrapper.sort.length == 1
                    && #wrapper.sort[0].contentEquals("sendBy")
                }
                """;
    }
}
