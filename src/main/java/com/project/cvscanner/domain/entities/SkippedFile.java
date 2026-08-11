package com.project.cvscanner.domain.entities;

import com.project.cvscanner.domain.entities.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "skipped_files")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SkippedFile extends BaseEntity {

    @Column(name = "batch_job_run_id", nullable = false)
    Long batchJobRunId;

    @Column(name = "file_name", nullable = false, length = 255)
    String fileName;

    @Column(name = "reason", length = 1000)
    String reason;
}