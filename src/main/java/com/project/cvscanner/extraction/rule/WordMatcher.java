package com.project.cvscanner.extraction.rule;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class WordMatcher {

    public boolean containsWholeWord(String text, String word) {
        String pattern = "\\b" + Pattern.quote(word) + "\\b";
        Matcher matcher = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text);
        return matcher.find();
    }
}
