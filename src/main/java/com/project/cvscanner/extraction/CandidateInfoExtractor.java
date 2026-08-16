package com.project.cvscanner.extraction;

import com.project.cvscanner.extraction.model.ExtractedCandidateInfo;
import com.project.cvscanner.extraction.rule.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandidateInfoExtractor {

    private final NameExtractionRule nameExtractionRule;
    private final ExperienceExtractionRule experienceExtractionRule;
    private final SkillsExtractionRule skillsExtractionRule;
    private final LocationExtractionRule locationExtractionRule;
    private final JobTypeExtractionRule jobTypeExtractionRule;

    public ExtractedCandidateInfo extract(String rawText) {
        return ExtractedCandidateInfo.builder()
                .fullName(nameExtractionRule.extract(rawText))
                .yearsOfExperience(experienceExtractionRule.extract(rawText))
                .skills(skillsExtractionRule.extract(rawText))
                .preferredLocation(locationExtractionRule.extract(rawText))
                .preferredJobType(jobTypeExtractionRule.extract(rawText))
                .build();
    }
}
