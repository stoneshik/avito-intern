CREATE TABLE teams (
    team_name varchar(100) PRIMARY KEY
);

CREATE TABLE users (
    user_id varchar(100) PRIMARY KEY,
    username varchar(100) NOT NULL UNIQUE,
    team_name varchar(100) NOT NULL,
    is_active boolean NOT NULL,
    FOREIGN KEY (team_name) REFERENCES teams(team_name) ON DELETE CASCADE
);

CREATE TYPE status_type AS ENUM ('OPEN', 'MERGED');
CREATE TABLE pull_requests (
    pull_request_id varchar(100) PRIMARY KEY,
    pull_request_name varchar(100) NOT NULL,
    author_id varchar(100) NOT NULL,
    status status_type NOT NULL,
    created_at timestamp NOT NULL default NOW(),
    merged_at timestamp default NULL,
    FOREIGN KEY (author_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE assignment_reviewers (
    user_id varchar(100),
    pull_request_id varchar(100),
    PRIMARY KEY (user_id, pull_request_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (pull_request_id) pull_requests ON DELETE CASCADE
);

-- Для быстрого поиска активных пользователей заданной команды (нужно для запроса создания pull request, запроса переназначения ревьювера)
CREATE INDEX idx_users_id_active ON USING HASH users(team_name, is_active);
-- Для быстрого поиска назначенных на pull request пользователей (нужно для запроса переназначения ревьювера)
CREATE INDEX idx_assignment_reviewers_pull_request_id ON USING_HASH assigned_reviewers(pull_request_id);
