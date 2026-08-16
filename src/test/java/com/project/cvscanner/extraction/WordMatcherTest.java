package com.project.cvscanner.extraction;

import com.project.cvscanner.extraction.rule.WordMatcher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordMatcherTest {

    private final WordMatcher wordMatcher = new WordMatcher();

    @Test
    void matchesWholeWordCaseInsensitively() {
        assertTrue(wordMatcher.containsWholeWord("I know JAVA well", "java"));
    }

    @Test
    void doesNotMatchSubstringInsideAnotherWord() {
        assertFalse(wordMatcher.containsWholeWord("Experienced in JavaScript", "Java"));
    }

    @Test
    void matchesWordsWithSpecialRegexCharacters() {
        assertTrue(wordMatcher.containsWholeWord("Skilled in C++ development", "C++"));
        assertTrue(wordMatcher.containsWholeWord("Built APIs with Node.js", "Node.js"));
    }

    @Test
    void matchesMultiWordPhrase() {
        assertTrue(wordMatcher.containsWholeWord("Experience with Spring Boot framework", "Spring Boot"));
    }

    @Test
    void returnsFalseWhenWordNotPresent() {
        assertFalse(wordMatcher.containsWholeWord("I like Python", "Java"));
    }
}
