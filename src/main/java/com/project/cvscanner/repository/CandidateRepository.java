package com.project.cvscanner.repository;

import com.project.cvscanner.domain.entities.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}
