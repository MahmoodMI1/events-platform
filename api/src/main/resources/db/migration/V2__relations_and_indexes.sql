-- === Step 1: rename & widen existing columns ===
ALTER TABLE events
    CHANGE COLUMN name       title       VARCHAR(120) NOT NULL,
    CHANGE COLUMN start_time start_at    DATETIME     NOT NULL,
    CHANGE COLUMN end_time   end_at      DATETIME     NOT NULL,
    MODIFY COLUMN location   VARCHAR(180) NOT NULL;

-- === Step 2: add new columns (nullable first to be safe) ===
ALTER TABLE events
    ADD COLUMN organizer_id BIGINT NULL         AFTER id,
    ADD COLUMN description  TEXT NULL           AFTER title,
    ADD COLUMN category     VARCHAR(60) NULL    AFTER description,
    ADD COLUMN status       ENUM('DRAFT','PUBLISHED','CANCELLED') NOT NULL DEFAULT 'DRAFT' AFTER category,
    ADD COLUMN updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER created_at;

-- === Step 3: constraints & indexes ===
ALTER TABLE events
    ADD CONSTRAINT fk_events_organizer
        FOREIGN KEY (organizer_id) REFERENCES users(id)
            ON DELETE RESTRICT ON UPDATE CASCADE,
    ADD CONSTRAINT chk_events_time
        CHECK (start_at < end_at);

CREATE INDEX idx_events_organizer ON events (organizer_id);
CREATE INDEX idx_events_start_at  ON events (start_at);
CREATE INDEX idx_events_status    ON events (status);
