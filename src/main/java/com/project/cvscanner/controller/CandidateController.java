package com.project.cvscanner.controller;

import com.project.cvscanner.dto.request.CandidateFilterRequest;
import com.project.cvscanner.dto.response.CandidateResponse;
import com.project.cvscanner.service.CandidateService;
import com.project.cvscanner.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/candidates")
@RequiredArgsConstructor
@Tag(name = "Candidates", description = "Operations related to candidates")
public class CandidateController {

    private final CandidateService candidateService;
    private final ExportService exportService;

    @Operation(summary = "Search Candidates", description = "Search for candidates based on filters")
    @GetMapping
    public Page<CandidateResponse> search(
            @Valid @ModelAttribute CandidateFilterRequest filter,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return candidateService.search(filter, pageable);
    }

    @Operation(summary = "Export Candidates to CSV", description = "Export candidate data to CSV format")
    @GetMapping("/export/csv")
    public void exportCsv(@Valid @ModelAttribute CandidateFilterRequest filter, HttpServletResponse response) throws IOException {
        exportService.exportToCsv(filter, response);
    }

    @Operation(summary = "Export Candidates to Excel", description = "Export candidate data to Excel format")
    @GetMapping("/export/excel")
    public void exportExcel(@Valid @ModelAttribute CandidateFilterRequest filter, HttpServletResponse response) throws IOException {
        exportService.exportToExcel(filter, response);
    }
}
