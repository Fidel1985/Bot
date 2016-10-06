CREATE SEQUENCE users_id_seq;
CREATE TABLE users (
  id        BIGINT PRIMARY KEY    NOT NULL,
  first_name      VARCHAR(50)           NOT NULL,
  last_name VARCHAR(50)           NOT NULL,
  username  VARCHAR(50) UNIQUE    NOT NULL,
  password  VARCHAR(255)          NOT NULL,
  email     VARCHAR(50)           NOT NULL
);
ALTER TABLE users ALTER COLUMN id SET DEFAULT nextval('users_id_seq');
ALTER TABLE users ADD CONSTRAINT users_email_key UNIQUE (email);
CREATE UNIQUE INDEX lower_users_username ON users (lower(username));
CREATE UNIQUE INDEX lower_users_email ON users (lower(email));

INSERT INTO users (first_name, last_name, username, password, email)
VALUES ('default', 'default', 'default',
        '$2a$10$IllOVQaB6IPZwDJZpWN7PORr9XU.O31h7vYvHWJUIN6nvioIzyWnu', 'default@mail.com');

CREATE SEQUENCE authority_id_seq;
CREATE TABLE authority (
  id   INT PRIMARY KEY    NOT NULL DEFAULT nextval('authority_id_seq'),
  name VARCHAR(50)        NOT NULL UNIQUE
);

CREATE UNIQUE INDEX lower_authority_name ON authority (lower(name));

CREATE TABLE user_authority_map (
  user_id      BIGINT REFERENCES users (id)     ON DELETE RESTRICT,
  authority_id INT    REFERENCES authority (id) ON DELETE RESTRICT
);

INSERT INTO authority (id, name)  VALUES (1, 'Admin'), (2, 'Project Manager');

INSERT INTO user_authority_map (user_id, authority_id) VALUES (1, 1), (1, 2);