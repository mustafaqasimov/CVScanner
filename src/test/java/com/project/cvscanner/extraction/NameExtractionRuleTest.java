package com.project.cvscanner.extraction;

import com.project.cvscanner.extraction.rule.NameExtractionRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NameExtractionRuleTest {

    private final NameExtractionRule rule = new NameExtractionRule();

    @Test
    void extractsSingleWordName() {
        String text = "Mustafa\nSoftware Engineer\nmustafa@email.com";
        assertEquals("Mustafa", rule.extract(text));
    }

    @Test
    void extractsFullName() {
        String text = "Mustafa Qasimov\nSoftware Engineer\nmustafa@email.com";
        assertEquals("Mustafa Qasimov", rule.extract(text));
    }

    @Test
    void skipsHeaderLineBeforeName() {
        String text = "Curriculum Vitae\n\nMustafa Qasimov\nSoftware Engineer";
        assertEquals("Mustafa Qasimov", rule.extract(text));
    }

    @Test
    void skipsContactInfoLineBeforeName() {
        String text = "mustafa@email.com\n+994 55 123 45 67\nMustafa Qasimov\nSoftware Engineer";
        assertEquals("Mustafa Qasimov", rule.extract(text));
    }

    @Test
    void skipsBlankLinesBeforeName() {
        String text = "\n\n\nMustafa Qasimov\nSoftware Engineer";
        assertEquals("Mustafa Qasimov", rule.extract(text));
    }

    @Test
    void doesNotMistakeJobTitleForName() {
        String text = "Software Engineer\nmustafa@email.com";
        assertNull(rule.extract(text));
    }

    @Test
    void returnsNullWhenAllLinesAreUppercase() {
        String text = "SOFTWARE ENGINEER\nSKILLS: JAVA, PYTHON\n5+ YEARS EXPERIENCE";
        assertNull(rule.extract(text));
    }

    @Test
    void returnsNullForEmptyText() {
        assertNull(rule.extract(""));
    }
}
