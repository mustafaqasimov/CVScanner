package com.project.cvscanner.domain.entities;

import com.project.cvscanner.domain.entities.base.BaseEntity;
import com.project.cvscanner.domain.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "batch_job_runs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BatchJobRun extends BaseEntity {

    @Column(name = "spring_batch_job_execution_id")
    Long springBatchJobExecutionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    JobStatus status = JobStatus.PENDING;

    @Column(name = "uploaded_by_user_id", nullable = false)
    Long uploadedByUserId;

    @Column(name = "total_files")
    Integer totalFiles;

    @Column(name = "success_count")
    @Builder.Default
    Integer successCount = 0;

    @Column(name = "failure_count")
    @Builder.Default
    Integer failureCount = 0;

    @Column(name = "started_at")
    LocalDateTime startedAt;

    @Column(name = "finished_at")
    LocalDateTime finishedAt;
}