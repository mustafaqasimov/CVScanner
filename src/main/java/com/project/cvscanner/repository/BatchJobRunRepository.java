package com.project.cvscanner.repository;

import com.project.cvscanner.domain.entities.BatchJobRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobRunRepository extends JpaRepository<BatchJobRun, Long> {
}
