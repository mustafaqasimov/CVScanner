package com.project.cvscanner.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Schema(description = "Request payload for filtering candidates")
public class CandidateFilterRequest {

    @Schema(description = "The skill to filter candidates by", example = "Java")
    @Size(max = 100, message = "Skill must be at most 100 characters")
    String skill;

    @Schema(description = "The minimum experience required", example = "5")
    @Min(value = 0, message = "Minimum experience cannot be negative")
    Integer minExperience;

    @Schema(description = "The location to filter candidates by", example = "New York")
    @Size(max = 150, message = "Location must be at most 150 characters")
    String location;

    @Schema(description = "The job type to filter candidates by", example = "Full-time")
    @Size(max = 50, message = "Job type must be at most 50 characters")
    String jobType;
}
