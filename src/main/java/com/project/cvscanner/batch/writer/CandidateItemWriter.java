package com.project.cvscanner.batch.writer;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandidateItemWriter implements ItemWriter<Candidate> {

    private final CandidateRepository candidateRepository;

    @Override
    public void write(Chunk<? extends Candidate> chunk) {
        candidateRepository.saveAll(chunk.getItems());
        log.info("Saved {} candidates to database", chunk.size());
    }
}
