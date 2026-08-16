package com.project.cvscanner.extraction.rule;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class NameExtractionRule {

    private static final int MAX_LINES_TO_CHECK = 6;

    private static final List<String> HEADER_WORDS = List.of(
            "resume", "curriculum vitae", "cv", "profile", "personal information"
    );

    private static final List<String> JOB_TITLE_INDICATORS = List.of(
            "engineer", "developer", "manager", "specialist", "analyst",
            "designer", "architect", "consultant", "director", "lead",
            "senior", "junior", "software", "full stack", "backend", "frontend"
    );

    private static final Pattern NAME_LIKE_PATTERN = Pattern.compile(
            "^[A-ZƏÖÜÇŞĞİ][a-zəöüçşğı]+(?:\\s+[A-ZƏÖÜÇŞĞİ][a-zəöüçşğı]+){0,3}$"
    );

    private static final Pattern CONTAINS_DIGIT = Pattern.compile(".*\\d.*");

    public String extract(String text) {
        String[] lines = text.split("\\r?\\n");
        int linesChecked = 0;

        for (String rawLine : lines) {
            if (linesChecked >= MAX_LINES_TO_CHECK) {
                break;
            }

            String line = rawLine.strip();
            if (line.isEmpty()) {
                continue;
            }

            linesChecked++;

            if (isHeaderLine(line) || isContactInfoLine(line)) {
                continue;
            }

            if (looksLikeAName(line)) {
                return line;
            }
        }

        return null;
    }

    private boolean isHeaderLine(String line) {
        String lower = line.toLowerCase();
        return HEADER_WORDS.stream().anyMatch(lower::contains);
    }

    private boolean isContactInfoLine(String line) {
        return line.contains("@") || CONTAINS_DIGIT.matcher(line).matches();
    }

    private boolean looksLikeAName(String line) {
        if (!NAME_LIKE_PATTERN.matcher(line).matches()) {
            return false;
        }
        String lower = line.toLowerCase();
        return JOB_TITLE_INDICATORS.stream().noneMatch(lower::contains);
    }
}
