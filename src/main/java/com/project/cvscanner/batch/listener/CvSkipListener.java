package com.project.cvscanner.batch.listener;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.domain.entities.SkippedFile;
import com.project.cvscanner.repository.SkippedFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.batch.core.listener.SkipListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
public class CvSkipListener implements SkipListener<Path, Candidate> {

    private final SkippedFileRepository skippedFileRepository;
    @Setter
    private Long batchJobRunId;

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        this.batchJobRunId = stepExecution.getJobParameters().getLong("batchJobRunId");
    }

    @OnSkipInRead
    public void onSkipInRead(Throwable t) {
        log.warn("Skipped a file during read: {}", t.getMessage());
        saveSkippedFile("unknown (read failure)", t);
    }

    @OnSkipInProcess
    public void onSkipInProcess(Path item, Throwable t) {
        log.warn("Skipped file during processing: {} - {}", item.getFileName(), t.getMessage());
        saveSkippedFile(item.getFileName().toString(), t);
    }

    @OnSkipInWrite
    public void onSkipInWrite(Candidate item, Throwable t) {
        log.warn("Skipped candidate during write: {} - {}", item.getSourceFileName(), t.getMessage());
        saveSkippedFile(item.getSourceFileName(), t);
    }

    private void saveSkippedFile(String fileName, Throwable t) {
        SkippedFile skippedFile = SkippedFile.builder()
                .batchJobRunId(batchJobRunId)
                .fileName(fileName)
                .reason(t.getMessage())
                .build();

        skippedFileRepository.save(skippedFile);
    }
}
