CREATE TABLE notion_folder
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT       NOT NULL,
    name    VARCHAR(100) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES uuser (id)
);

CREATE TABLE notion
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    notion_folder_id BIGINT                   NOT NULL,
    name             VARCHAR(100)             NOT NULL,
    content          VARCHAR(2000) DEFAULT '' NOT NULL,
    FOREIGN KEY (notion_folder_id) REFERENCES notion_folder (id)
);

CREATE TABLE edge
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    source_notion_id BIGINT                  NOT NULL,
    target_notion_id BIGINT                  NOT NULL,
    description      VARCHAR(200) DEFAULT '' NOT NULL,
    UNIQUE (source_notion_id, target_notion_id),
    FOREIGN KEY (source_notion_id) REFERENCES notion (id),
    FOREIGN KEY (target_notion_id) REFERENCES notion (id)
);

CREATE TABLE uuser
(
    created_at DATETIME(3) NOT NULL,
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    password   VARCHAR(30)  NOT NULL,
    email      VARCHAR(150) NOT NULL
);
