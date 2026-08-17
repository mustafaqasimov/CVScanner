package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.domain.entities.User;
import com.project.cvscanner.domain.enums.JobStatus;
import com.project.cvscanner.domain.enums.Role;
import com.project.cvscanner.dto.response.JobStatusResponse;
import com.project.cvscanner.exception.error.ResourceNotFoundException;
import com.project.cvscanner.exception.error.UnauthorizedException;
import com.project.cvscanner.mapper.BatchJobRunMapper;
import com.project.cvscanner.repository.BatchJobRunRepository;
import com.project.cvscanner.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobStatusServiceTest {

    @Mock
    private BatchJobRunRepository batchJobRunRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BatchJobRunMapper batchJobRunMapper;

    @InjectMocks
    private JobStatusService jobStatusService;

    @Test
    void getStatus_Success_OwnerOfJob() {
        Long jobId = 1L;
        Long userId = 100L;

        BatchJobRun batchJobRun = BatchJobRun.builder()
                .id(jobId)
                .uploadedByUserId(userId)
                .status(JobStatus.PENDING)
                .build();

        JobStatusResponse expectedResponse = JobStatusResponse.builder()
                .jobId(jobId)
                .status(JobStatus.PENDING)
                .build();

        when(batchJobRunRepository.findById(jobId)).thenReturn(Optional.of(batchJobRun));
        when(batchJobRunMapper.toResponse(batchJobRun)).thenReturn(expectedResponse);

        JobStatusResponse response = jobStatusService.getStatus(jobId, userId);

        // Then
        assertNotNull(response);
        assertEquals(jobId, response.getJobId());
        assertEquals(JobStatus.PENDING, response.getStatus());

        verify(batchJobRunRepository, times(1)).findById(jobId);
        verifyNoInteractions(userRepository);
        verify(batchJobRunMapper, times(1)).toResponse(batchJobRun);
    }

    @Test
    void getStatus_Success_AdminUser() {
        // Given
        Long jobId = 1L;
        Long ownerId = 100L;
        Long adminId = 200L;

        BatchJobRun batchJobRun = BatchJobRun.builder()
                .id(jobId)
                .uploadedByUserId(ownerId)
                .status(JobStatus.COMPLETED)
                .build();

        User adminUser = User.builder()
                .id(adminId)
                .role(Role.ROLE_ADMIN)
                .build();

        JobStatusResponse expectedResponse = JobStatusResponse.builder()
                .jobId(jobId)
                .status(JobStatus.COMPLETED)
                .build();

        when(batchJobRunRepository.findById(jobId)).thenReturn(Optional.of(batchJobRun));
        when(userRepository.findById(adminId)).thenReturn(Optional.of(adminUser));
        when(batchJobRunMapper.toResponse(batchJobRun)).thenReturn(expectedResponse);

        // When
        JobStatusResponse response = jobStatusService.getStatus(jobId, adminId);

        // Then
        assertNotNull(response);
        assertEquals(jobId, response.getJobId());

        verify(batchJobRunRepository, times(1)).findById(jobId);
        verify(userRepository, times(1)).findById(adminId);
        verify(batchJobRunMapper, times(1)).toResponse(batchJobRun);
    }

    @Test
    void getStatus_JobNotFound_ThrowsException() {
        // Given
        Long jobId = 99L;
        Long userId = 100L;

        when(batchJobRunRepository.findById(jobId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> jobStatusService.getStatus(jobId, userId)
        );

        assertEquals("Job not found: " + jobId, exception.getMessage());
        verify(batchJobRunRepository, times(1)).findById(jobId);
        verifyNoInteractions(userRepository, batchJobRunMapper);
    }

    @Test
    void getStatus_UnauthorizedUser_ThrowsException() {
        // Given
        Long jobId = 1L;
        Long ownerId = 100L;
        Long unauthorizedUserId = 200L;

        BatchJobRun batchJobRun = BatchJobRun.builder()
                .id(jobId)
                .uploadedByUserId(ownerId)
                .build();

        User normalUser = User.builder()
                .id(unauthorizedUserId)
                .role(Role.ROLE_USER)
                .build();

        when(batchJobRunRepository.findById(jobId)).thenReturn(Optional.of(batchJobRun));
        when(userRepository.findById(unauthorizedUserId)).thenReturn(Optional.of(normalUser));

        // When & Then
        UnauthorizedException exception = assertThrows(
                UnauthorizedException.class,
                () -> jobStatusService.getStatus(jobId, unauthorizedUserId)
        );

        assertEquals("You cannot view this job's status", exception.getMessage());
        verify(batchJobRunRepository, times(1)).findById(jobId);
        verify(userRepository, times(1)).findById(unauthorizedUserId);
        verifyNoInteractions(batchJobRunMapper);
    }

    @Test
    void getStatus_UserNotFoundForAccessCheck_ThrowsException() {
        // Given
        Long jobId = 1L;
        Long ownerId = 100L;
        Long unknownUserId = 300L;

        BatchJobRun batchJobRun = BatchJobRun.builder()
                .id(jobId)
                .uploadedByUserId(ownerId)
                .build();

        when(batchJobRunRepository.findById(jobId)).thenReturn(Optional.of(batchJobRun));
        when(userRepository.findById(unknownUserId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> jobStatusService.getStatus(jobId, unknownUserId)
        );

        assertEquals("User not found", exception.getMessage());
        verify(batchJobRunRepository, times(1)).findById(jobId);
        verify(userRepository, times(1)).findById(unknownUserId);
        verifyNoInteractions(batchJobRunMapper);
    }
}
