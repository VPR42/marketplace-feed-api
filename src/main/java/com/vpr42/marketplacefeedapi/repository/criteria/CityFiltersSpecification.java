package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CityFiltersSpecification {
    public static Specification<CityEntity> filtered(boolean withJobs,
                                              boolean sortByJobsCount,
                                              String searchQuery) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Expression<String> concatOne = cb.concat(root.get("region"), " ");
            Expression<String> concatTwo = cb.concat(concatOne, root.get("name"));
            if (withJobs || sortByJobsCount) {
                Join<CityEntity, UserEntity> user = root.join("users", JoinType.LEFT);
                JoinType type = withJobs ? JoinType.INNER : JoinType.LEFT;
                Join<UserEntity, JobEntity> jobs = user.join("jobs", type);
                query.groupBy(root);
                if (sortByJobsCount) {
                    query.orderBy(
                        cb.desc(cb.coalesce(cb.count(jobs), 0)),
                        cb.asc(concatTwo)
                    );
                }
            }

            if (!sortByJobsCount) {
                query.orderBy(
                    cb.asc(concatTwo)
                );
            }

            if (searchQuery != null && !searchQuery.isBlank()) {
                String q = "%" + searchQuery.toLowerCase() + "%";
                predicates.add(cb.like(
                    cb.lower(concatTwo),
                    q
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
