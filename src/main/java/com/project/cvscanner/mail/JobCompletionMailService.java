package com.project.cvscanner.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobCompletionMailService {

    private final JavaMailSender mailSender;

    public void sendJobCompletionEmail(String toEmail, Long jobId, int totalFiles, int successCount, int failureCount) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("CV Processing Job #" + jobId + " Completed");
            message.setText("""
                    Your CV batch job has finished processing.
                    
                    Total files: %d
                    Successfully processed: %d
                    Failed/skipped: %d
                    """.formatted(totalFiles, successCount, failureCount));

            mailSender.send(message);
            log.info("Job completion email sent to {} for jobId={}", toEmail, jobId);

        } catch (Exception e) {
            log.error("Failed to send job completion email to {} for jobId={}", toEmail, jobId, e);
        }
    }
}
