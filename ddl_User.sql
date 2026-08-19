CREATE TABLE users
(
    id        BINARY(16)   NOT NULL,
    username  VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    firstname VARCHAR(255) NULL,
    lastname  VARCHAR(255) NULL,
    dob       date         NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE users_roles
(
    user_id    BINARY(16)   NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users_roles PRIMARY KEY (user_id, roles_name)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (roles_name) REFERENCES `role` (name);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);