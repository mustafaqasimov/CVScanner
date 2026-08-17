package com.project.cvscanner.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class CandidateResponse {
    Long id;
    String fullName;
    Integer yearsOfExperience;
    List<String> skills;
    String preferredJobType;
    String preferredLocation;
    String sourceFileName;
}
