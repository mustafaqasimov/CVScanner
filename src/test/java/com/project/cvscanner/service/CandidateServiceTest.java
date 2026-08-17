package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.dto.request.CandidateFilterRequest;
import com.project.cvscanner.dto.response.CandidateResponse;
import com.project.cvscanner.mapper.CandidateMapper;
import com.project.cvscanner.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private CandidateMapper candidateMapper;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    void search_Success() {
        // Given
        CandidateFilterRequest filter = CandidateFilterRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Candidate candidate = new Candidate();
        candidate.setId(1L);

        CandidateResponse responseDto = CandidateResponse.builder()
                .id(1L)
                .build();

        Page<Candidate> candidatePage = new PageImpl<>(List.of(candidate), pageable, 1);

        when(candidateRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(candidatePage);
        when(candidateMapper.toResponse(candidate)).thenReturn(responseDto);

        // When
        Page<CandidateResponse> result = candidateService.search(filter, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());

        verify(candidateRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verify(candidateMapper, times(1)).toResponse(candidate);
    }

    @Test
    void search_EmptyResult() {
        // Given
        CandidateFilterRequest filter = CandidateFilterRequest.builder().build();
        Pageable pageable = PageRequest.of(0, 10);

        Page<Candidate> emptyPage = Page.empty(pageable);

        when(candidateRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(emptyPage);

        // When
        Page<CandidateResponse> result = candidateService.search(filter, pageable);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());

        verify(candidateRepository, times(1)).findAll(any(Specification.class), eq(pageable));
        verifyNoInteractions(candidateMapper);
    }
}
