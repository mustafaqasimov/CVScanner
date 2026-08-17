package com.project.cvscanner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
class CvScannerApplicationTests {

    @BeforeAll
    static void loadEnvVariables() {
        try {
            Path envPath = Path.of(".env");
            if (Files.exists(envPath)) {
                List<String> lines = Files.readAllLines(envPath);
                for (String line : lines) {
                    if (line != null && !line.isBlank() && !line.trim().startsWith("#")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();

                            // Əgər sistemdə hələ təyin olunmayıbsa, .env-dən oxuyub əlavə edirik
                            if (System.getProperty(key) == null && System.getenv(key) == null) {
                                System.setProperty(key, value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Could not load .env file for tests: " + e.getMessage());
        }
    }

    @Test
    void contextLoads() {
    }

}
