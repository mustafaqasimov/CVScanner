package com.project.cvscanner.extraction.rule;

import com.project.cvscanner.config.CvExtractionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationExtractionRule {

    private final WordMatcher wordMatcher;
    private final CvExtractionProperties properties;

    public String extract(String text) {
        for (String location : properties.getKnownLocations()) {
            if (wordMatcher.containsWholeWord(text, location)) {
                return location;
            }
        }
        return null;
    }
}
