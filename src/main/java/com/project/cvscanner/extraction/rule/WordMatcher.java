package com.project.cvscanner.extraction.rule;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class WordMatcher {

    public boolean containsWholeWord(String text, String keyword) {
        if (text == null || keyword == null || keyword.isBlank()) {
            return false;
        }

        String escapedKeyword = Pattern.quote(keyword);

        String regex = "(?i)(?<!\\w)" + escapedKeyword + "(?!\\w)";

        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(text).find();
    }
}
