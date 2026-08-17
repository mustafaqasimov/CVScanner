package com.project.cvscanner.dto.response;

import com.project.cvscanner.domain.enums.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Response payload for job status")
public class JobStatusResponse {
    @Schema(description = "The ID of the job")
    Long jobId;
    @Schema(description = "The status of the job")
    JobStatus status;
    @Schema(description = "The total number of files")
    Integer totalFiles;
    @Schema(description = "The number of successful operations")
    Integer successCount;
    @Schema(description = "The number of failed operations")
    Integer failureCount;
    @Schema(description = "The time when the job was started")
    LocalDateTime startedAt;
    @Schema(description = "The time when the job was finished")
    LocalDateTime finishedAt;
}
