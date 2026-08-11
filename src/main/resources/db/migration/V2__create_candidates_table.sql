CREATE TABLE candidates
(
    id                  BIGSERIAL PRIMARY KEY,
    active              VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    version             BIGINT        NOT NULL DEFAULT 0,
    created_at          TIMESTAMP,
    updated_at          TIMESTAMP,
    full_name           VARCHAR(200),
    years_of_experience INTEGER,
    preferred_job_type  VARCHAR(50),
    preferred_location  VARCHAR(150),
    source_file_name    VARCHAR(255)  NOT NULL,
    batch_job_run_id    BIGINT        NOT NULL
);

CREATE TABLE candidate_skills
(
    candidate_id BIGINT       NOT NULL REFERENCES candidates (id) ON DELETE CASCADE,
    skill        VARCHAR(100) NOT NULL
);

CREATE INDEX idx_candidate_skills_skill ON candidate_skills (skill);
CREATE INDEX idx_candidates_batch_job_run_id ON candidates (batch_job_run_id);