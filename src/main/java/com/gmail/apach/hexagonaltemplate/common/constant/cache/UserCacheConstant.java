package com.gmail.apach.hexagonaltemplate.common.constant.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserCacheConstant {

    public static final String CACHE_NAME = "user";
    public static final String LIST_CACHE_NAME = "users";

    public static class Key {

        public static final String ID = "#userId";
        public static final String USER__ID = "#user.userId";
        public static final String SEARCH = "{#wrapper.page, #wrapper.size, #context.username}";

    }

    public static class Condition {

        public static final String SEARCH = """ 
            {
                #wrapper.username == null
                && #wrapper.firstName == null
                && #wrapper.lastName == null
                && #wrapper.email == null
                && #wrapper.enabled == null
                && #wrapper.createdFrom == null
                && #wrapper.createdTo == null
                && #wrapper.createdBy == null
                && #wrapper.sort != null
                && #wrapper.sort.length == 1
                && #wrapper.sort[0].contentEquals("created")
            }
            """;
    }
}
