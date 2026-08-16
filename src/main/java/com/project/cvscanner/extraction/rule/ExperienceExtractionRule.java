package com.project.cvscanner.extraction.rule;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExperienceExtractionRule {

    // "5 years", "5+ years", "5 yrs", "5-year" kimi formatları tutur
    private static final Pattern EXPERIENCE_PATTERN = Pattern.compile(
            "(\\d{1,2})\\+?\\s*(?:years?|yrs?)\\b",
            Pattern.CASE_INSENSITIVE
    );

    public Integer extract(String text) {
        Matcher matcher = EXPERIENCE_PATTERN.matcher(text);

        int maxExperience = 0;
        boolean found = false;

        while (matcher.find()) {
            int years = Integer.parseInt(matcher.group(1));
            if (years > maxExperience) {
                maxExperience = years;
                found = true;
            }
        }

        return found ? maxExperience : null;
    }
}
