package com.project.cvscanner.batch.config;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfig {

    @Bean
    public JobOperator jobOperator(JobRepository jobRepository) throws Exception {
        TaskExecutorJobOperator operator = new TaskExecutorJobOperator();
        operator.setJobRepository(jobRepository);
        operator.setTaskExecutor(new SimpleAsyncTaskExecutor("batch-job-"));
        operator.afterPropertiesSet();
        return operator;
    }
}