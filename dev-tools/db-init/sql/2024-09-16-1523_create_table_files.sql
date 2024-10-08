CREATE TABLE files (
    file_        varchar(36)                         NOT NULL,
    storage_key  text                                NOT NULL,
    file_name    varchar(500)                        NOT NULL,
    content_type varchar(500)                        NOT NULL,
    file_size    bigint                              NOT NULL,
    created      timestamp                           NOT NULL,
    CONSTRAINT files_pkey PRIMARY KEY (file_)
);