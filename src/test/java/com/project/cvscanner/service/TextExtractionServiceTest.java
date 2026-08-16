package com.project.cvscanner.service;

import com.project.cvscanner.exception.error.CvParsingException;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TextExtractionServiceTest {

    @Mock
    private Tika tika;

    @InjectMocks
    private TextExtractionService service;

    @Test
    void wrapsIOExceptionInCvParsingException() throws Exception {
        Path fakePath = Path.of("corrupted.pdf");
        when(tika.parseToString(fakePath.toFile())).thenThrow(new IOException("corrupted"));

        assertThrows(CvParsingException.class, () -> service.extractText(fakePath));
    }
}
