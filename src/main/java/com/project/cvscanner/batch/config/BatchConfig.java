package com.project.cvscanner.batch.config;

import org.springframework.batch.core.launch.support.JobOperatorFactoryBean;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfig {

    @Primary
    @Bean(name = "cvScannerJobOperator")
    public JobOperatorFactoryBean cvScannerJobOperator(JobRepository jobRepository) {
        JobOperatorFactoryBean factoryBean = new JobOperatorFactoryBean();
        factoryBean.setJobRepository(jobRepository);
        factoryBean.setTaskExecutor(new SimpleAsyncTaskExecutor("batch-job-"));
        return factoryBean;
    }
}