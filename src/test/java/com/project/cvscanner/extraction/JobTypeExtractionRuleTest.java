package com.project.cvscanner.extraction;

import com.project.cvscanner.config.CvExtractionProperties;
import com.project.cvscanner.extraction.rule.JobTypeExtractionRule;
import com.project.cvscanner.extraction.rule.WordMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class JobTypeExtractionRuleTest {

    private JobTypeExtractionRule rule;

    @BeforeEach
    void setUp() {
        Map<String, String> keywords = new LinkedHashMap<>();
        keywords.put("remote", "Remote");
        keywords.put("hybrid", "Hybrid");
        keywords.put("onsite", "Onsite");

        CvExtractionProperties properties = new CvExtractionProperties();
        properties.setJobTypeKeywords(keywords);

        rule = new JobTypeExtractionRule(new WordMatcher(), properties);
    }

    @Test
    void extractsRemotePreference() {
        assertEquals("Remote", rule.extract("Looking for remote positions only"));
    }

    @Test
    void extractsHybridPreference() {
        assertEquals("Hybrid", rule.extract("Prefer hybrid work arrangement"));
    }

    @Test
    void returnsFirstMatchingKeywordWhenMultiplePresent() {
        String text = "Prefer remote work, but open to onsite if necessary";
        assertEquals("Remote", rule.extract(text));
    }

    @Test
    void isCaseInsensitive() {
        assertEquals("Onsite", rule.extract("Available for ONSITE work"));
    }

    @Test
    void returnsNullWhenNoPreferenceKeywordMentioned() {
        assertNull(rule.extract("Experienced software developer with strong skills"));
    }
}
