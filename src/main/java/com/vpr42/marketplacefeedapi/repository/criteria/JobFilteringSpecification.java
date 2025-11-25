package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobFilteringSpecification extends BaseJobFilteringSpecification {

    public static Specification<JobEntity> filter(JobFilters filters) {
        return (root, query, cb) -> {
            query.select(root.get("id"));

            // Join-ы специфичные для JobFilters
            Join<JobEntity, MasterInfoEntity> master = root.join("masterInfo");
            Join<MasterInfoEntity, UserEntity> user = null;
            Join<UserEntity, CityEntity> city = null;

            if (filters.getCityId() != null && filters.getCityId() > 0) {
                user = master.join("user");
                city = user.join("city");
            }

            List<Predicate> whereStatements = new ArrayList<>();

            if (filters.getMasterId() != null) {
                whereStatements.add(cb.equal(master.get("id"), filters.getMasterId()));
            }
            if (filters.getExperience() != null && filters.getExperience() > 0) {
                whereStatements.add(
                        cb.greaterThanOrEqualTo(
                                master.get("experience"),
                                (long) filters.getExperience()
                        )
                );
            }
            if (filters.getCityId() != null && filters.getCityId() > 0) {
                whereStatements.add(cb.equal(city.get("id"), filters.getCityId()));
            }

            applyCommonFilters(
                    root, query, cb,
                    filters.getCategoryId(),
                    filters.getMinPrice(),
                    filters.getMaxPrice(),
                    filters.getQuery(),
                    filters.getTags(),
                    filters.getSkills(),
                    filters.getPriceSort(),
                    filters.getOrdersCountSort(),
                    null,
                    filters.getMinOrders()
            );

            applyJobSpecificSorting(root, query, cb, master, filters);

            if (!whereStatements.isEmpty()) {
                Predicate commonPredicate = query.getRestriction();
                Predicate specificPredicate = cb.and(whereStatements.toArray(new Predicate[0]));
                query.where(cb.and(commonPredicate, specificPredicate));
            }
            return query.getRestriction();
        };
    }

    private static void applyJobSpecificSorting(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            Join<JobEntity, MasterInfoEntity> master,
            JobFilters filters) {

        List<Order> sorts = new ArrayList<>();

        if (filters.getExperienceSort() != null) {
            sorts.add(
                    filters.getExperienceSort() == SortType.ASC
                            ? cb.asc(master.get("experience"))
                            : cb.desc(master.get("experience"))
            );
        }

        if (!sorts.isEmpty()) {
            List<Order> existingOrders = query.getOrderList();
            if (existingOrders != null) {
                sorts.addAll(0, existingOrders);
            }
            query.orderBy(sorts);
        }
    }
}