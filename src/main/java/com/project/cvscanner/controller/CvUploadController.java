package com.project.cvscanner.controller;

import com.project.cvscanner.dto.response.UploadResponse;
import com.project.cvscanner.security.AuthFacade;
import com.project.cvscanner.service.CvUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/cv")
@RequiredArgsConstructor
public class CvUploadController {

    private final CvUploadService cvUploadService;
    private final AuthFacade authFacade;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<UploadResponse> upload(@RequestParam("file") MultipartFile file) {
        Long currentUserId = authFacade.getCurrentUserId();
        UploadResponse response = cvUploadService.uploadAndProcess(file, currentUserId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
