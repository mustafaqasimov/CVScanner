package com.project.cvscanner.domain.entities;

import com.project.cvscanner.domain.entities.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "candidates")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Candidate extends BaseEntity {

    @Column(name = "full_name", length = 200)
    String fullName;

    @Column(name = "years_of_experience")
    Integer yearsOfExperience;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "candidate_skills", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "skill", length = 100)
    @Builder.Default
    List<String> skills = new java.util.ArrayList<>();

    @Column(name = "preferred_job_type", length = 50)
    String preferredJobType;

    @Column(name = "preferred_location", length = 150)
    String preferredLocation;

    @Column(name = "source_file_name", nullable = false, length = 255)
    String sourceFileName;

    @Column(name = "batch_job_run_id", nullable = false)
    Long batchJobRunId;
}
