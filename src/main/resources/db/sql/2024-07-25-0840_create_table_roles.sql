CREATE TABLE roles (
    role_ varchar(36)                         NOT NULL,
    role  varchar(255)                        NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (role_),
    CONSTRAINT roles_role_uk UNIQUE (role)
);