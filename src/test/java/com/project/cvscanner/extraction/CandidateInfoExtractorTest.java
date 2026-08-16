package com.project.cvscanner.extraction;

import com.project.cvscanner.extraction.model.ExtractedCandidateInfo;
import com.project.cvscanner.extraction.rule.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateInfoExtractorTest {

    @Mock
    private NameExtractionRule nameExtractionRule;
    @Mock private ExperienceExtractionRule experienceExtractionRule;
    @Mock private SkillsExtractionRule skillsExtractionRule;
    @Mock private LocationExtractionRule locationExtractionRule;
    @Mock private JobTypeExtractionRule jobTypeExtractionRule;

    @InjectMocks
    private CandidateInfoExtractor extractor;

    @Test
    void combinesAllRuleResultsIntoOneObject() {
        String text = "some CV text";

        when(nameExtractionRule.extract(text)).thenReturn("John Smith");
        when(experienceExtractionRule.extract(text)).thenReturn(5);
        when(skillsExtractionRule.extract(text)).thenReturn(List.of("Java", "Spring Boot"));
        when(locationExtractionRule.extract(text)).thenReturn("Baku");
        when(jobTypeExtractionRule.extract(text)).thenReturn("Remote");

        ExtractedCandidateInfo result = extractor.extract(text);

        assertEquals("John Smith", result.getFullName());
        assertEquals(5, result.getYearsOfExperience());
        assertEquals(List.of("Java", "Spring Boot"), result.getSkills());
        assertEquals("Baku", result.getPreferredLocation());
        assertEquals("Remote", result.getPreferredJobType());
    }
}
