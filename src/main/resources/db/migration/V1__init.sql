CREATE TABLE IF NOT EXISTS users
(
    user_id             TEXT            PRIMARY KEY,
    email               VARCHAR(255)    UNIQUE NOT NULL,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    last_login          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_credentials
(
    credential_id       TEXT            PRIMARY KEY,
    user_id             TEXT            NOT NULL,
    password_hash       VARCHAR(255)    NOT NULL,
    created_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS user_oath
(
    oauth_id                TEXT            PRIMARY KEY,
    user_id                 TEXT            NOT NULL,
    provider                VARCHAR(100)    NOT NULL,
    provider_user_id        TEXT            NOT NULL,
    provider_access_token   TEXT            NOT NULL,
    provider_refresh_token  TEXT            NULL,
    created_at              TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE IF NOT EXISTS role
(
    role_id             SERIAL          PRIMARY KEY,
    role                VARCHAR(50)     NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id             TEXT            NOT NULL,
    role_id             INTEGER         NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);