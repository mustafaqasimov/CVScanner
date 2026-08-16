package com.project.cvscanner.batch.processor;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.extraction.CandidateInfoExtractor;
import com.project.cvscanner.extraction.model.ExtractedCandidateInfo;
import com.project.cvscanner.mapper.CandidateMapper;
import com.project.cvscanner.service.TextExtractionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Slf4j
@Component
@StepScope
@RequiredArgsConstructor
public class CvFileItemProcessor implements ItemProcessor<Path, Candidate> {

    private final TextExtractionService textExtractionService;
    private final CandidateInfoExtractor candidateInfoExtractor;
    private final CandidateMapper candidateMapper;

    @Value("#{jobParameters['batchJobRunId']}")
    private Long batchJobRunId;

    @Override
    public Candidate process(Path filePath) {
        log.debug("Processing file: {}", filePath.getFileName());

        String rawText = textExtractionService.extractText(filePath);
        ExtractedCandidateInfo extractedInfo = candidateInfoExtractor.extract(rawText);

        return candidateMapper.toEntity(extractedInfo, filePath.getFileName().toString(), batchJobRunId);
    }
}
