package com.project.cvscanner.controller;

import com.project.cvscanner.dto.response.JobStatusResponse;
import com.project.cvscanner.security.AuthFacade;
import com.project.cvscanner.service.JobStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobStatusService jobStatusService;
    private final AuthFacade authFacade;

    @GetMapping("/{id}")
    public JobStatusResponse getStatus(@PathVariable Long id) {
        Long currentUserId = authFacade.getCurrentUserId();
        return jobStatusService.getStatus(id, currentUserId);
    }
}
