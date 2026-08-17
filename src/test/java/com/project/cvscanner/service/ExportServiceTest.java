package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.dto.request.CandidateFilterRequest;
import com.project.cvscanner.repository.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExportServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ExportService exportService;

    @Test
    void exportToCsv_Success() throws Exception {
        // Given
        CandidateFilterRequest filter = CandidateFilterRequest.builder().build();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Candidate candidate = Candidate.builder()
                .id(1L)
                .fullName("John Doe")
                .yearsOfExperience(3)
                .skills(List.of("Java", "Spring Boot"))
                .preferredJobType("Remote")
                .preferredLocation("Baku")
                .sourceFileName("resume.pdf")
                .build();

        when(candidateRepository.findAll(any(Specification.class))).thenReturn(List.of(candidate));

        exportService.exportToCsv(filter, response);

        assertEquals("text/csv", response.getContentType());
        assertEquals("attachment; filename=candidates.csv", response.getHeader("Content-Disposition"));

        String outputContent = response.getContentAsString();
        assertTrue(outputContent.contains("ID,Full Name,Years of Experience,Skills,Preferred Job Type,Preferred Location,Source File"));
        assertTrue(outputContent.contains("1,John Doe,3,Java; Spring Boot,Remote,Baku,resume.pdf"));

        verify(candidateRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void exportToExcel_Success() throws Exception {
        CandidateFilterRequest filter = CandidateFilterRequest.builder().build();
        MockHttpServletResponse response = new MockHttpServletResponse();

        Candidate candidate = Candidate.builder()
                .id(1L)
                .fullName("Jane Doe")
                .yearsOfExperience(5)
                .skills(List.of("Python", "Django"))
                .preferredJobType("Hybrid")
                .preferredLocation("Baku")
                .sourceFileName("cv.docx")
                .build();

        when(candidateRepository.findAll(any(Specification.class))).thenReturn(List.of(candidate));

        exportService.exportToExcel(filter, response);

        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", response.getContentType());
        assertEquals("attachment; filename=candidates.xlsx", response.getHeader("Content-Disposition"));

        byte[] content = response.getContentAsByteArray();
        assertNotNull(content);
        assertTrue(content.length > 0, "Excel faylının bayt massivi boş olmamalıdır");

        verify(candidateRepository, times(1)).findAll(any(Specification.class));
    }
}
