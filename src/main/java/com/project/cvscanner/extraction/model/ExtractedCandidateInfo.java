package com.project.cvscanner.extraction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ExtractedCandidateInfo {
    String fullName;
    Integer yearsOfExperience;
    List<String> skills;
    String preferredJobType;
    String preferredLocation;
}
