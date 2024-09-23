ALTER TABLE notion_folder
    ADD COLUMN created_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    ADD COLUMN last_modified_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);

ALTER TABLE notion
    ADD COLUMN created_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    ADD COLUMN last_modified_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);

ALTER TABLE edge
    ADD COLUMN created_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    ADD COLUMN last_modified_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);

ALTER TABLE uuser
    MODIFY COLUMN created_at timestamp(3) NOT NULL,
    ADD COLUMN last_modified_at timestamp(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3);

ALTER TABLE notion_folder
    ALTER COLUMN created_at DROP DEFAULT,
    ALTER COLUMN last_modified_at DROP DEFAULT;

ALTER TABLE notion
    ALTER COLUMN created_at DROP DEFAULT,
    ALTER COLUMN last_modified_at DROP DEFAULT;

ALTER TABLE edge
    ALTER COLUMN created_at DROP DEFAULT,
    ALTER COLUMN last_modified_at DROP DEFAULT;

ALTER TABLE uuser
    ALTER COLUMN last_modified_at DROP DEFAULT;

