package com.project.cvscanner.controller;

import com.project.cvscanner.dto.response.JobStatusResponse;
import com.project.cvscanner.security.AuthFacade;
import com.project.cvscanner.service.JobStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
@Tag(name = "Jobs", description = "Operations related to job status")
public class JobController {

    private final JobStatusService jobStatusService;
    private final AuthFacade authFacade;

    @Operation(summary = "Get job status", description = "Retrieve the status of a specific job")
    @GetMapping("/{id}")
    public JobStatusResponse getStatus(@PathVariable Long id) {
        Long currentUserId = authFacade.getCurrentUserId();
        return jobStatusService.getStatus(id, currentUserId);
    }
}
