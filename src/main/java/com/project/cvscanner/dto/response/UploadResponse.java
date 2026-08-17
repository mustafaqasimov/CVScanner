package com.project.cvscanner.dto.response;

import com.project.cvscanner.domain.enums.JobStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "Response payload for upload operations")
public class UploadResponse {
    @Schema(description = "The ID of the uploaded job")
    Long jobId;
    @Schema(description = "The status of the upload")
    JobStatus status;
    @Schema(description = "A message describing the result of the upload")
    String message;
}
