package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.domain.enums.Role;
import com.project.cvscanner.dto.response.JobStatusResponse;
import com.project.cvscanner.exception.error.ResourceNotFoundException;
import com.project.cvscanner.exception.error.UnauthorizedException;
import com.project.cvscanner.mapper.BatchJobRunMapper;
import com.project.cvscanner.repository.BatchJobRunRepository;
import com.project.cvscanner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JobStatusService {

    private final BatchJobRunRepository batchJobRunRepository;
    private final UserRepository userRepository;
    private final BatchJobRunMapper batchJobRunMapper;

    public JobStatusResponse getStatus(Long jobId, Long currentUserId) {
        BatchJobRun batchJobRun = batchJobRunRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found: " + jobId));

        if (!hasAccess(batchJobRun, currentUserId)) {
            throw new UnauthorizedException("You cannot view this job's status");
        }

        return batchJobRunMapper.toResponse(batchJobRun);
    }

    private boolean hasAccess(BatchJobRun batchJobRun, Long currentUserId) {
        if (batchJobRun.getUploadedByUserId().equals(currentUserId)) {
            return true;
        }

        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return currentUser.getRole() == Role.ROLE_ADMIN;
    }
}
