package com.project.cvscanner.batch.listener;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.domain.enums.JobStatus;
import com.project.cvscanner.exception.error.ResourceNotFoundException;
import com.project.cvscanner.mail.JobCompletionMailService;
import com.project.cvscanner.repository.BatchJobRunRepository;
import com.project.cvscanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CvJobExecutionerListener implements JobExecutionListener {

    private final BatchJobRunRepository batchJobRunRepository;
    private final UserRepository userRepository;
    private final JobCompletionMailService jobCompletionMailService;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        Long batchJobRunId = jobExecution.getJobParameters().getLong("batchJobRunId");

        BatchJobRun batchJobRun = batchJobRunRepository.findById(batchJobRunId)
                .orElseThrow(() -> new ResourceNotFoundException("BatchJobRun not found: " + batchJobRunId));

        batchJobRun.setStatus(JobStatus.RUNNING);
        batchJobRun.setStartedAt(LocalDateTime.now());
        batchJobRun.setSpringBatchJobExecutionId(jobExecution.getId());

        batchJobRunRepository.save(batchJobRun);

        log.info("Batch job started - batchJobRunId={}, springBatchJobExecutionId={}",
                batchJobRunId, jobExecution.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Long batchJobRunId = jobExecution.getJobParameters().getLong("batchJobRunId");

        BatchJobRun batchJobRun = batchJobRunRepository.findById(batchJobRunId)
                .orElseThrow(() -> new ResourceNotFoundException("BatchJobRun not found: " + batchJobRunId));

        int successCount = 0;
        int failureCount = 0;

        for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
            successCount += (int) stepExecution.getWriteCount();
            failureCount += (int) stepExecution.getSkipCount();
        }

        JobStatus finalStatus = jobExecution.getStatus() == BatchStatus.COMPLETED
                ? JobStatus.COMPLETED
                : JobStatus.FAILED;

        batchJobRun.setStatus(finalStatus);
        batchJobRun.setFinishedAt(LocalDateTime.now());
        batchJobRun.setSuccessCount(successCount);
        batchJobRun.setFailureCount(failureCount);
        batchJobRun.setTotalFiles(successCount + failureCount);

        batchJobRunRepository.save(batchJobRun);

        log.info("Batch job finished - batchJobRunId={}, status={}, success={}, failed={}",
                batchJobRunId, finalStatus, successCount, failureCount);

        notifyUser(batchJobRun);
    }

    private void notifyUser(BatchJobRun batchJobRun) {
        User uploader = userRepository.findById(batchJobRun.getUploadedByUserId()).orElse(null);

        if (uploader == null) {
            log.warn("Could not send completion email - uploader user not found: {}",
                    batchJobRun.getUploadedByUserId());
            return;
        }

        jobCompletionMailService.sendJobCompletionEmail(
                uploader.getEmail(),
                batchJobRun.getId(),
                batchJobRun.getTotalFiles(),
                batchJobRun.getSuccessCount(),
                batchJobRun.getFailureCount()
        );
    }
}
