package com.project.cvscanner.extraction.rule;

import com.project.cvscanner.config.CvExtractionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JobTypeExtractionRule {

    private final WordMatcher wordMatcher;
    private final CvExtractionProperties properties;

    public String extract(String text) {
        for (Map.Entry<String, String> entry : properties.getJobTypeKeywords().entrySet()) {
            if (wordMatcher.containsWholeWord(text, entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }
}
