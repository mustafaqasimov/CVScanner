package com.project.cvscanner.extraction.rule;

import com.project.cvscanner.config.CvExtractionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkillsExtractionRule {

    private final WordMatcher wordMatcher;
    private final CvExtractionProperties properties;

    public List<String> extract(String text) {
        List<String> foundSkills = new ArrayList<>();

        for (String skill : properties.getKnownSkills()) {
            if (wordMatcher.containsWholeWord(text, skill)) {
                foundSkills.add(skill);
            }
        }

        return foundSkills;
    }
}
