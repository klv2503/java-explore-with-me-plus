DROP TABLE IF EXISTS participation_request;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS locations;
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

CREATE TABLE IF NOT EXISTS locations (
       id     BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
       lat    decimal,
       lon    decimal
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  title VARCHAR(120) NOT NULL,
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT,
  description VARCHAR(7000) NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE,
  location_id BIGINT,
  is_paid boolean,
  participant_limit BIGINT,
  request_moderation boolean,
  creation_date TIMESTAMP WITHOUT TIME ZONE,
  publication_date TIMESTAMP WITHOUT TIME ZONE,
  state VARCHAR(50) NOT NULL,
  views BIGINT,
  initiator_id BIGINT,
  FOREIGN KEY (category_id) REFERENCES category (id),
  FOREIGN KEY (initiator_id) REFERENCES users (id),
  FOREIGN KEY (location_id) REFERENCES locations (id)
);

CREATE TABLE IF NOT EXISTS participation_request
(
    id       BIGINT      NOT NULL GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id  BIGINT      NOT NULL,
    event_id BIGINT      NOT NULL,
    status   VARCHAR(50) NOT NULL,
    created  TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (event_id) REFERENCES events (id) ON DELETE CASCADE,
    CONSTRAINT unique_user_event UNIQUE (user_id, event_id) -- Запрещает дублирующиеся заявки
);