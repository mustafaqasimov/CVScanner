package com.project.cvscanner.dto.response;

import com.project.cvscanner.domain.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class JobStatusResponse {
    Long jobId;
    JobStatus status;
    Integer totalFiles;
    Integer successCount;
    Integer failureCount;
    LocalDateTime startedAt;
    LocalDateTime finishedAt;
}
