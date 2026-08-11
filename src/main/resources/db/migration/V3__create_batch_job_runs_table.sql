CREATE TABLE batch_job_runs
(
    id                          BIGSERIAL PRIMARY KEY,
    active                      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    version                     BIGINT       NOT NULL DEFAULT 0,
    created_at                  TIMESTAMP,
    updated_at                  TIMESTAMP,
    spring_batch_job_execution_id BIGINT,
    status                      VARCHAR(20)  NOT NULL,
    uploaded_by_user_id         BIGINT       NOT NULL,
    total_files                 INTEGER,
    success_count               INTEGER      NOT NULL DEFAULT 0,
    failure_count                INTEGER      NOT NULL DEFAULT 0,
    started_at                  TIMESTAMP,
    finished_at                 TIMESTAMP
);