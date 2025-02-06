DROP TABLE IF EXISTS participation_request;
DROP TABLE IF EXISTS event;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
       id     BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       name   VARCHAR(250) NOT NULL,
       email  VARCHAR(254) NOT NULL UNIQUE  --пока поставил unique, удалим, если будет лишним
);

CREATE TABLE IF NOT EXISTS category (
       id     BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       name   VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS event (
       id                 BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       initiator_id       BIGINT NOT NULL,
       state              VARCHAR(50) NOT NULL,
       request_moderation BOOLEAN NOT NULL,
       participant_limit  INT NOT NULL,
       confirmed_requests INT NOT NULL DEFAULT 0,
       FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS participation_request (
       id          BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       user_id     BIGINT NOT NULL,
       event_id    BIGINT NOT NULL,
       status      VARCHAR(50) NOT NULL,
       created     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
       FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE,
       CONSTRAINT unique_user_event UNIQUE (user_id, event_id) -- Запрещает дублирующиеся заявки
);
