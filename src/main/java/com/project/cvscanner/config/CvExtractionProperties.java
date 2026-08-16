package com.project.cvscanner.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "cv-extraction")
public class CvExtractionProperties {

    private List<String> knownSkills = List.of();
    private List<String> knownLocations = List.of();
    private Map<String, String> jobTypeKeywords = new LinkedHashMap<>();
}
