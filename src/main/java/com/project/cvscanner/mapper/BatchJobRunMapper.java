package com.project.cvscanner.mapper;

import com.project.cvscanner.domain.entities.BatchJobRun;
import com.project.cvscanner.dto.response.JobStatusResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BatchJobRunMapper {

    @Mapping(target = "jobId", source = "id")
    JobStatusResponse toResponse(BatchJobRun batchJobRun);
}
