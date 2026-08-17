package com.project.cvscanner.service;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.domain.specification.CandidateSpecification;
import com.project.cvscanner.dto.request.CandidateFilterRequest;
import com.project.cvscanner.repository.CandidateRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExportService {

    private static final String[] HEADERS = {
            "ID", "Full Name", "Years of Experience", "Skills", "Preferred Job Type", "Preferred Location", "Source File"
    };

    private final CandidateRepository candidateRepository;

    public void exportToCsv(CandidateFilterRequest filter, HttpServletResponse response) throws IOException {
        List<Candidate> candidates = fetchFiltered(filter);

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=candidates.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println(String.join(",", HEADERS));

            for (Candidate c : candidates) {
                writer.println(String.join(",",
                        escapeCsv(String.valueOf(c.getId())),
                        escapeCsv(c.getFullName()),
                        escapeCsv(String.valueOf(c.getYearsOfExperience())),
                        escapeCsv(String.join("; ", c.getSkills())),
                        escapeCsv(c.getPreferredJobType()),
                        escapeCsv(c.getPreferredLocation()),
                        escapeCsv(c.getSourceFileName())
                ));
            }
        }
    }

    public void exportToExcel(CandidateFilterRequest filter, HttpServletResponse response) throws IOException {
        List<Candidate> candidates = fetchFiltered(filter);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=candidates.xlsx");

        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Candidates");

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < HEADERS.length; i++) {
                headerRow.createCell(i).setCellValue(HEADERS[i]);
            }

            int rowIndex = 1;
            for (Candidate c : candidates) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(c.getId());
                row.createCell(1).setCellValue(nullSafe(c.getFullName()));
                row.createCell(2).setCellValue(c.getYearsOfExperience() != null ? c.getYearsOfExperience() : 0);
                row.createCell(3).setCellValue(String.join("; ", c.getSkills()));
                row.createCell(4).setCellValue(nullSafe(c.getPreferredJobType()));
                row.createCell(5).setCellValue(nullSafe(c.getPreferredLocation()));
                row.createCell(6).setCellValue(nullSafe(c.getSourceFileName()));
            }

            for (int i = 0; i < HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(response.getOutputStream());
        }
    }

    private List<Candidate> fetchFiltered(CandidateFilterRequest filter) {
        return candidateRepository.findAll(CandidateSpecification.withFilters(filter));
    }

    private String nullSafe(String value) {
        return value != null ? value : "";
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
