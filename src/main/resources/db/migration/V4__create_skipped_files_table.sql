CREATE TABLE skipped_files
(
    id               BIGSERIAL PRIMARY KEY,
    active           VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    version          BIGINT       NOT NULL DEFAULT 0,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    batch_job_run_id BIGINT       NOT NULL REFERENCES batch_job_runs (id),
    file_name        VARCHAR(255) NOT NULL,
    reason           VARCHAR(1000)
);