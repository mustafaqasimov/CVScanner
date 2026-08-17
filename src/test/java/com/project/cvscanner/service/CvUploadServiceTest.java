package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.domain.enums.JobStatus;
import com.project.cvscanner.dto.response.UploadResponse;
import com.project.cvscanner.exception.error.InvalidUploadException;
import com.project.cvscanner.repository.BatchJobRunRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CvUploadServiceTest {

    @Mock
    private BatchJobRunRepository batchJobRunRepository;

    @Mock
    private JobOperator jobOperator;

    @Mock
    private Job parseCvJob;

    @InjectMocks
    private CvUploadService cvUploadService;

    @Test
    void uploadAndProcess_Success() throws Exception {
        byte[] zipBytes = createSampleZipFile();
        MockMultipartFile zipFile = new MockMultipartFile(
                "file",
                "resumes.zip",
                "application/zip",
                zipBytes
        );

        Long userId = 1L;

        BatchJobRun savedBatchJob = BatchJobRun.builder()
                .id(100L)
                .status(JobStatus.PENDING)
                .uploadedByUserId(userId)
                .build();

        when(batchJobRunRepository.save(any(BatchJobRun.class))).thenReturn(savedBatchJob);
        JobExecution mockJobExecution = mock(JobExecution.class);
        lenient().when(mockJobExecution.getId()).thenReturn(100L);

        when(jobOperator.start(eq(parseCvJob), any())).thenReturn(mockJobExecution);

        UploadResponse response = cvUploadService.uploadAndProcess(zipFile, userId);

        assertNotNull(response);
        assertEquals(100L, response.getJobId());
        assertEquals(JobStatus.PENDING, response.getStatus());
        assertEquals("Upload accepted, processing started", response.getMessage());

        verify(batchJobRunRepository, times(1)).save(any(BatchJobRun.class));
        verify(jobOperator, times(1)).start(eq(parseCvJob), any(JobParameters.class));
    }

    @Test
    void uploadAndProcess_EmptyFile_ThrowsException() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "empty.zip",
                "application/zip",
                new byte[0]
        );

        InvalidUploadException exception = assertThrows(
                InvalidUploadException.class,
                () -> cvUploadService.uploadAndProcess(emptyFile, 1L)
        );

        assertEquals("Uploaded file is empty", exception.getMessage());
        verifyNoInteractions(batchJobRunRepository, jobOperator);
    }

    @Test
    void uploadAndProcess_InvalidExtension_ThrowsException() {
        MockMultipartFile txtFile = new MockMultipartFile(
                "file",
                "document.txt",
                "text/plain",
                "some text".getBytes()
        );

        InvalidUploadException exception = assertThrows(
                InvalidUploadException.class,
                () -> cvUploadService.uploadAndProcess(txtFile, 1L)
        );

        assertEquals("Only .zip files are accepted", exception.getMessage());
        verifyNoInteractions(batchJobRunRepository, jobOperator);
    }

    private byte[] createSampleZipFile() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            zos.putNextEntry(new ZipEntry("cv1.pdf"));
            zos.write("Simulated PDF content".getBytes());
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("notes.txt"));
            zos.write("Some notes".getBytes());
            zos.closeEntry();
        }
        return baos.toByteArray();
    }
}
