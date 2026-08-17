package com.project.cvscanner.batch;

import com.project.cvscanner.batch.processor.CvFileItemProcessor;
import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.extraction.CandidateInfoExtractor;
import com.project.cvscanner.extraction.model.ExtractedCandidateInfo;
import com.project.cvscanner.mapper.CandidateMapper;
import com.project.cvscanner.service.TextExtractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvFileItemProcessorTest {

    @Mock
    private TextExtractionService textExtractionService;

    @Mock
    private CandidateInfoExtractor candidateInfoExtractor;

    @Mock
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CvFileItemProcessor cvFileItemProcessor;

    private final Long batchJobRunId = 10L;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cvFileItemProcessor, "batchJobRunId", batchJobRunId);
    }

    @Test
    void process_Success() {
        // Given
        Path filePath = Path.of("uploads/job-10/resume.pdf");
        String rawText = "John Doe - Java Developer with 3 years of experience";

        ExtractedCandidateInfo extractedInfo = ExtractedCandidateInfo.builder()
                .fullName("John Doe")
                .build();

        Candidate expectedCandidate = Candidate.builder()
                .id(1L)
                .fullName("John Doe")
                .sourceFileName("resume.pdf")
                .build();

        when(textExtractionService.extractText(filePath)).thenReturn(rawText);
        when(candidateInfoExtractor.extract(rawText)).thenReturn(extractedInfo);
        when(candidateMapper.toEntity(extractedInfo, "resume.pdf", batchJobRunId)).thenReturn(expectedCandidate);

        // When
        Candidate result = cvFileItemProcessor.process(filePath);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getFullName());
        assertEquals("resume.pdf", result.getSourceFileName());

        // Verifications
        verify(textExtractionService, times(1)).extractText(filePath);
        verify(candidateInfoExtractor, times(1)).extract(rawText);
        verify(candidateMapper, times(1)).toEntity(extractedInfo, "resume.pdf", batchJobRunId);
    }
}
