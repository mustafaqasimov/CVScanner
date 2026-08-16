package com.project.cvscanner.mapper;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.extraction.model.ExtractedCandidateInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "fullName", source = "info.fullName")
    @Mapping(target = "yearsOfExperience", source = "info.yearsOfExperience")
    @Mapping(target = "skills", source = "info.skills")
    @Mapping(target = "preferredJobType", source = "info.preferredJobType")
    @Mapping(target = "preferredLocation", source = "info.preferredLocation")
    @Mapping(target = "sourceFileName", source = "sourceFileName")
    @Mapping(target = "batchJobRunId", source = "batchJobRunId")
    Candidate toEntity(ExtractedCandidateInfo info, String sourceFileName, Long batchJobRunId);
}
