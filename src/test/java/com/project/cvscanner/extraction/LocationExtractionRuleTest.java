package com.project.cvscanner.extraction;

import com.project.cvscanner.config.CvExtractionProperties;
import com.project.cvscanner.extraction.rule.LocationExtractionRule;
import com.project.cvscanner.extraction.rule.WordMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LocationExtractionRuleTest {

    private LocationExtractionRule rule;

    @BeforeEach
    void setUp() {
        CvExtractionProperties properties = new CvExtractionProperties();
        properties.setKnownLocations(List.of("Baku", "Remote", "Istanbul", "London"));

        rule = new LocationExtractionRule(new WordMatcher(), properties);
    }

    @Test
    void extractsKnownCity() {
        assertEquals("Baku", rule.extract("Currently based in Baku, looking for opportunities"));
    }

    @Test
    void extractsRemoteAsLocation() {
        assertEquals("Remote", rule.extract("Open to Remote positions worldwide"));
    }

    @Test
    void isCaseInsensitive() {
        assertEquals("Istanbul", rule.extract("Living in istanbul currently"));
    }

    @Test
    void returnsFirstMatchWhenMultipleLocationsPresent() {
        String text = "Previously worked in London, now based in Baku";
        assertEquals("Baku", rule.extract(text));
    }

    @Test
    void returnsNullWhenNoKnownLocationMentioned() {
        assertNull(rule.extract("I live in a small village"));
    }
}
