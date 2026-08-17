package com.project.cvscanner.batch;

import com.project.cvscanner.batch.listener.CvSkipListener;
import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.domain.entities.SkippedFile;
import com.project.cvscanner.repository.SkippedFileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.step.StepExecution;


import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvSkipListenerTest {

    @Mock
    private SkippedFileRepository skippedFileRepository;

    @Mock
    private StepExecution stepExecution;

    @Mock
    private JobParameters jobParameters;

    @InjectMocks
    private CvSkipListener cvSkipListener;

    private final Long batchJobRunId = 15L;

    @BeforeEach
    void setUp() {
        when(stepExecution.getJobParameters()).thenReturn(jobParameters);
        when(jobParameters.getLong("batchJobRunId")).thenReturn(batchJobRunId);

        cvSkipListener.beforeStep(stepExecution);
    }

    @Test
    void beforeStep_ShouldExtractBatchJobRunId() {
        // Given
        CvSkipListener listener = new CvSkipListener(skippedFileRepository);
        StepExecution customStepExecution = mock(StepExecution.class);
        JobParameters customJobParameters = mock(JobParameters.class);

        when(customStepExecution.getJobParameters()).thenReturn(customJobParameters);
        when(customJobParameters.getLong("batchJobRunId")).thenReturn(55L);

        // When
        listener.beforeStep(customStepExecution);
        listener.onSkipInRead(new RuntimeException("Test read error"));

        // Then
        ArgumentCaptor<SkippedFile> captor = ArgumentCaptor.forClass(SkippedFile.class);
        verify(skippedFileRepository).save(captor.capture());
        assertEquals(55L, captor.getValue().getBatchJobRunId());
    }

    @Test
    void onSkipInRead_ShouldSaveSkippedFile() {
        // Given
        Throwable ex = new RuntimeException("File read failed");

        // When
        cvSkipListener.onSkipInRead(ex);

        // Then
        ArgumentCaptor<SkippedFile> captor = ArgumentCaptor.forClass(SkippedFile.class);
        verify(skippedFileRepository, times(1)).save(captor.capture());

        SkippedFile saved = captor.getValue();
        assertEquals(batchJobRunId, saved.getBatchJobRunId());
        assertEquals("unknown (read failure)", saved.getFileName());
        assertEquals("File read failed", saved.getReason());
    }

    @Test
    void onSkipInProcess_ShouldSaveSkippedFile() {
        // Given
        Path path = Path.of("uploads/job-15/invalid_cv.pdf");
        Throwable ex = new RuntimeException("Parsing error");

        // When
        cvSkipListener.onSkipInProcess(path, ex);

        // Then
        ArgumentCaptor<SkippedFile> captor = ArgumentCaptor.forClass(SkippedFile.class);
        verify(skippedFileRepository, times(1)).save(captor.capture());

        SkippedFile saved = captor.getValue();
        assertEquals(batchJobRunId, saved.getBatchJobRunId());
        assertEquals("invalid_cv.pdf", saved.getFileName());
        assertEquals("Parsing error", saved.getReason());
    }

    @Test
    void onSkipInWrite_ShouldSaveSkippedFile() {
        // Given
        Candidate candidate = Candidate.builder()
                .sourceFileName("resume_error.docx")
                .build();
        Throwable ex = new RuntimeException("Database constraint violation");

        // When
        cvSkipListener.onSkipInWrite(candidate, ex);

        // Then
        ArgumentCaptor<SkippedFile> captor = ArgumentCaptor.forClass(SkippedFile.class);
        verify(skippedFileRepository, times(1)).save(captor.capture());

        SkippedFile saved = captor.getValue();
        assertEquals(batchJobRunId, saved.getBatchJobRunId());
        assertEquals("resume_error.docx", saved.getFileName());
        assertEquals("Database constraint violation", saved.getReason());
    }
}
