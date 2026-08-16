package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.domain.enums.JobStatus;
import com.project.cvscanner.dto.response.UploadResponse;
import com.project.cvscanner.exception.error.InvalidUploadException;
import com.project.cvscanner.repository.BatchJobRunRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CvUploadService {

    private static final String UPLOAD_ROOT = "uploads";

    private final BatchJobRunRepository batchJobRunRepository;
    private final JobOperator jobOperator;
    private final Job parseCvJob;

    public UploadResponse uploadAndProcess(MultipartFile zipFile, Long uploadedByUserId) {
        validateZipFile(zipFile);

        BatchJobRun batchJobRun = BatchJobRun.builder()
                .status(JobStatus.PENDING)
                .uploadedByUserId(uploadedByUserId)
                .build();
        batchJobRun = batchJobRunRepository.save(batchJobRun);

        Path uploadDir = extractZip(zipFile, batchJobRun.getId());

        launchJobAsync(batchJobRun.getId(), uploadDir);

        return UploadResponse.builder()
                .jobId(batchJobRun.getId())
                .status(JobStatus.PENDING)
                .message("Upload accepted, processing started")
                .build();
    }

    private void validateZipFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidUploadException("Uploaded file is empty");
        }
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".zip")) {
            throw new InvalidUploadException("Only .zip files are accepted");
        }
    }

    private Path extractZip(MultipartFile zipFile, Long batchJobRunId) {
        Path targetDir = Path.of(UPLOAD_ROOT, "job-" + batchJobRunId);

        try {
            Files.createDirectories(targetDir);

            try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    if (entry.isDirectory()) {
                        continue;
                    }

                    Path entryPath = resolveSafely(targetDir, entry.getName());
                    if (!isSupportedFileType(entryPath)) {
                        continue;
                    }

                    Files.createDirectories(entryPath.getParent());
                    Files.copy(zis, entryPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }

            return targetDir;

        } catch (IOException e) {
            log.error("Failed to extract zip for batchJobRunId={}", batchJobRunId, e);
            throw new InvalidUploadException("Could not extract zip file: " + e.getMessage());
        }
    }

    private Path resolveSafely(Path targetDir, String entryName) {
        Path resolved = targetDir.resolve(entryName).normalize();
        if (!resolved.startsWith(targetDir)) {
            throw new InvalidUploadException("Zip entry is outside the target directory: " + entryName);
        }
        return resolved;
    }

    private boolean isSupportedFileType(Path path) {
        String name = path.getFileName().toString().toLowerCase();
        return name.endsWith(".pdf") || name.endsWith(".docx") || name.endsWith(".doc");
    }

    private void launchJobAsync(Long batchJobRunId, Path uploadDir) {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("batchJobRunId", batchJobRunId)
                    .addString("uploadDir", uploadDir.toAbsolutePath().toString())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            jobOperator.start(parseCvJob, jobParameters);

        } catch (Exception e) {
            log.error("Failed to launch batch job for batchJobRunId={}", batchJobRunId, e);
            throw new IllegalStateException("Could not start CV processing job", e);
        }
    }
}
