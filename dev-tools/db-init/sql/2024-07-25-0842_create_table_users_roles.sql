CREATE TABLE users_roles (
    user_      varchar(36)                            NOT NULL,
    role_      varchar(36)                            NOT NULL,
    CONSTRAINT users_roles_pkey PRIMARY KEY (user_, role_),
    CONSTRAINT users_roles_user_fkey FOREIGN KEY (user_) REFERENCES users (user_),
    CONSTRAINT users_roles_role_fkey FOREIGN KEY (role_) REFERENCES roles (role_)
);