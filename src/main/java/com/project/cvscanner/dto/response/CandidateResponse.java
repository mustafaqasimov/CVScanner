package com.project.cvscanner.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateResponse {
    Long id;
    Long batchJobId;
    String fullName;
    String email;
    String phone;
    String location;
    List<String> skills;
    Integer experienceYears;
    String preferredJob;
    String sourceFileName;
}
