package com.project.cvscanner.extraction;

import com.project.cvscanner.extraction.rule.ExperienceExtractionRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ExperienceExtractionRuleTest {

    private final ExperienceExtractionRule rule = new ExperienceExtractionRule();

    @Test
    void extracts_simple_years_pattern() {
        assertEquals(5, rule.extract("I have 5 years of experience in Java"));
    }

    @Test
    void extracts_plus_notation() {
        assertEquals(7, rule.extract("7+ years of software development"));
    }

    @Test
    void picks_the_largest_number_when_multiple_matches() {
        assertEquals(8, rule.extract("2 years as Junior Dev, 8 years total experience"));
    }

    @Test
    void returns_null_when_no_match() {
        assertNull(rule.extract("Experienced software engineer with strong Java skills"));
    }
}
