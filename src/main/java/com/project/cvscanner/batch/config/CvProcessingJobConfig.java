package com.project.cvscanner.batch.config;

import com.project.cvscanner.batch.listener.CvJobExecutionerListener;
import com.project.cvscanner.batch.listener.CvSkipListener;
import com.project.cvscanner.batch.processor.CvFileItemProcessor;
import com.project.cvscanner.batch.reader.CvFileItemReader;
import com.project.cvscanner.batch.writer.CandidateItemWriter;
import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.exception.error.CvParsingException;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.file.Path;

@Configuration
@RequiredArgsConstructor
public class CvProcessingJobConfig {

    private static final int CHUNK_SIZE = 10;
    private static final int SKIP_LIMIT = 1000;
    private static final int RETRY_LIMIT = 3;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    private final CvFileItemReader cvFileItemReader;
    private final CvFileItemProcessor cvFileItemProcessor;
    private final CandidateItemWriter candidateItemWriter;
    private final CvSkipListener cvSkipListener;
    private final CvJobExecutionerListener cvJobExecutionListener;

    @Bean
    public Job parseCvJob() {
        return new JobBuilder("parseCvJob", jobRepository)
                .listener(cvJobExecutionListener)
                .start(parseCvStep())
                .build();
    }

    @Bean
    public Step parseCvStep() {
        return new StepBuilder("parseCvStep", jobRepository)
                .<Path, Candidate>chunk(CHUNK_SIZE)
                .reader(cvFileItemReader)
                .processor(cvFileItemProcessor)
                .writer(candidateItemWriter)
                .transactionManager(transactionManager)
                .faultTolerant()
                .skip(CvParsingException.class)
                .skipLimit(SKIP_LIMIT)
                .listener(cvSkipListener)
                .retry(java.io.IOException.class)
                .retryLimit(RETRY_LIMIT)
                .build();
    }
}
