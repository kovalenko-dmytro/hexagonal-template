CREATE TABLE users (
    user_      varchar(36)                         NOT NULL,
    user_name  varchar(255)                        NOT NULL,
    password   varchar(255)                        NOT NULL,
    first_name varchar(50)                         NULL,
    last_name  varchar(50)                         NULL,
    email      varchar(255)                        NOT NULL,
    enabled    bool                                NOT NULL,
    created    timestamp                           NOT NULL,
    created_by varchar(255)                        NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (user_),
    CONSTRAINT users_user_name_uk UNIQUE (user_name)
);