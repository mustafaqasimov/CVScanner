package com.project.cvscanner.repository;

import com.project.cvscanner.domain.entities.SkippedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkippedFileRepository extends JpaRepository<SkippedFile, Long> {
}
