package com.project.cvscanner.domain.specification;

import com.project.cvscanner.domain.entities.Candidate;
import com.project.cvscanner.dto.request.CandidateFilterRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class CandidateSpecification {

    public static Specification<Candidate> withFilters(CandidateFilterRequest filter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getSkill() != null && !filter.getSkill().isBlank()) {
                Join<Object, Object> skillsJoin = root.join("skills");
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(skillsJoin.as(String.class)), filter.getSkill().toLowerCase()));
                query.distinct(true);
            }

            if (filter.getMinExperience() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(root.get("yearsOfExperience"), filter.getMinExperience()));
            }

            if (filter.getLocation() != null && !filter.getLocation().isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("preferredLocation")), filter.getLocation().toLowerCase()));
            }

            if (filter.getJobType() != null && !filter.getJobType().isBlank()) {
                predicate = cb.and(predicate,
                        cb.equal(cb.lower(root.get("preferredJobType")), filter.getJobType().toLowerCase()));
            }

            return predicate;
        };
    }
}
