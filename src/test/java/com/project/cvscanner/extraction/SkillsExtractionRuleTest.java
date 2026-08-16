package com.project.cvscanner.extraction;

import com.project.cvscanner.config.CvExtractionProperties;
import com.project.cvscanner.extraction.rule.SkillsExtractionRule;
import com.project.cvscanner.extraction.rule.WordMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SkillsExtractionRuleTest {

    private SkillsExtractionRule rule;

    @BeforeEach
    void setUp() {
        CvExtractionProperties properties = new CvExtractionProperties();
        properties.setKnownSkills(List.of("Java", "React", "Spring Boot"));

        rule = new SkillsExtractionRule(new WordMatcher(), properties);
    }

    @Test
    void extracts_known_skills_case_insensitively() {
        List<String> result = rule.extract("I have experience with java and REACT");
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("React"));
    }
}
