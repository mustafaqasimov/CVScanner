package com.project.cvscanner.batch.reader;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@StepScope
public class CvFileItemReader implements ItemReader<Path> {

    private Iterator<Path> fileIterator;

    @Value("#{jobParameters['uploadDir']}")
    private String uploadDir;

    @Override
    public Path read() {
        if (fileIterator == null) {
            fileIterator = loadFilesFromDirectory().iterator();
        }

        return fileIterator.hasNext() ? fileIterator.next() : null;
    }

    private List<Path> loadFilesFromDirectory() {
        Path dir = Path.of(uploadDir);

        try (Stream<Path> paths = Files.walk(dir)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(this::isSupportedFileType)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to list files in upload directory: {}", uploadDir, e);
            throw new IllegalStateException("Could not read upload directory: " + uploadDir, e);
        }
    }

    private boolean isSupportedFileType(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".doc");
    }
}
