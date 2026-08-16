package com.project.cvscanner.dto.response;

import com.project.cvscanner.domain.enums.JobStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UploadResponse {
    Long jobId;
    JobStatus status;
    String message;
}
