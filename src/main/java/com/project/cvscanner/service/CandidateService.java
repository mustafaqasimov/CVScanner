package com.project.cvscanner.service;

import com.project.cvscanner.domain.specification.CandidateSpecification;
import com.project.cvscanner.dto.request.CandidateFilterRequest;
import com.project.cvscanner.dto.response.CandidateResponse;
import com.project.cvscanner.mapper.CandidateMapper;
import com.project.cvscanner.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    public Page<CandidateResponse> search(CandidateFilterRequest filter, Pageable pageable) {
        return candidateRepository.findAll(CandidateSpecification.withFilters(filter), pageable)
                .map(candidateMapper::toResponse);
    }
}
