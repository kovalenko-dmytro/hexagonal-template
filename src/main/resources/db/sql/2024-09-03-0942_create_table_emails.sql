CREATE TABLE public."emails" (
    email_        varchar(36)                         NOT NULL,
    send_by       varchar(255)                        NOT NULL,
    send_to       varchar(255)                        NOT NULL,
    send_to_cc    varchar[],
    subject       varchar(255),
    send_time     timestamp                           NOT NULL,
    email_type    varchar(50)                         NOT NULL,
    email_status  varchar(50)                         NOT NULL,
    CONSTRAINT emails_pkey PRIMARY KEY (email_)
);