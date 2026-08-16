package com.project.cvscanner.service;

import com.project.cvscanner.exception.error.CvParsingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Service
@RequiredArgsConstructor
public class TextExtractionService {

    private final Tika tika;

    public String extractText(Path filePath) {
        try {
            return tika.parseToString(filePath.toFile());
        } catch (IOException | TikaException e) {
            log.warn("Failed to extract text from file: {}", filePath.getFileName(), e);
            throw new CvParsingException("Could not extract text from file: " + filePath.getFileName(), e);
        }
    }
}
