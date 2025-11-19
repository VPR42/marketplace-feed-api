package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.OrderEntity;
import com.vpr42.marketplacefeedapi.model.entity.SkillEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class JobFilteringSpecification {
    public static Specification<JobEntity> filter(JobFilters filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            Join<JobEntity, MasterInfoEntity> masterInfo = root.join("masterInfo");;
            Join<JobEntity, TagEntity> tags = null;
            Join<JobEntity, OrderEntity> orders = null;
            Join<MasterInfoEntity, UserEntity> user = null;
            Join<MasterInfoEntity, SkillEntity> skills = null;
            Join<UserEntity, CityEntity> city = null;

            boolean hasTags = filters.getTags() != null && filters.getTags().length > 0;
            boolean hasSkills = filters.getSkills() != null && filters.getSkills().length > 0;

            // Билдим необходимые Join-ы
            if (filters.getMasterId() != null
                    || filters.getExperience() != null
                    || filters.getExperienceSort() != null
                    || hasSkills
                    || filters.getCityId() != null
                    || filters.getQuery() != null) {
                masterInfo = root.join("masterInfo");
            }

            if (filters.getCityId() != null) {
                user = masterInfo.join("user");
                city = user.join("city");
            }
            if (hasSkills) {
                skills = masterInfo.join("skills", JoinType.LEFT);
                query.distinct(true);
            }
            if (hasTags) {
                tags = root.join("tags", JoinType.LEFT);
                query.distinct(true);
            }

            if (filters.getMinOrders() != null || filters.getOrdersCountSort() != null) {
                orders = root.join("orders", JoinType.LEFT);
            }

            // Билдим запрос
            if (filters.getMasterId() != null) {
                predicates.add(cb.equal(masterInfo.get("id"), filters.getMasterId()));
            }
            if (filters.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("category").get("id"), filters.getCategoryId()));
            }
            if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
                predicates.add(
                    cb.between(
                        root.get("price"),
                        filters.getMinPrice() != null ? filters.getMinPrice() : 0,
                        filters.getMaxPrice() != null ? filters.getMaxPrice() : 999999
                    )
                );
            }
            if (filters.getCityId() != null) {
                predicates.add(cb.equal(city.get("id"), filters.getCityId()));
            }
            if (filters.getExperience() != null) {
                predicates.add(
                    cb.greaterThanOrEqualTo(
                        masterInfo.get("experience"),
                        filters.getExperience()
                    )
                );
            }
            if (filters.getQuery() != null && !filters.getQuery().isBlank()) {
                String likeQuery = "%" + filters.getQuery().toLowerCase() + "%";
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("name")), likeQuery),
                        cb.like(cb.lower(cb.coalesce(masterInfo.get("pseudonym"), "")), likeQuery)
                    )
                );
            }

            // Группировка и having
            boolean hasOrdersJoin = orders != null;
            List<Predicate> havings = new ArrayList<>();

            if (hasTags || hasSkills || hasOrdersJoin) {
                query.groupBy(root.get("id"));
            }

            if (hasTags) {
                predicates.add(tags.get("name").in((Object[]) filters.getTags()));
                havings.add(cb.equal(cb.countDistinct(tags.get("name")), filters.getTags().length));
            }
            if (hasSkills) {
                predicates.add(skills.get("name").in((Object[]) filters.getSkills()));
                havings.add(cb.equal(cb.countDistinct(skills.get("name")), filters.getSkills().length));
            }
            if (hasOrdersJoin) {
                havings.add(cb.greaterThanOrEqualTo(cb.countDistinct(orders), filters.getMinOrders().longValue()));
            }

            if (!havings.isEmpty()) {
                query.having(cb.and(havings.toArray(new Predicate[0])));
            }

            // Сортировка
            List<Order> sorts = new ArrayList<>();
            if (filters.getPriceSort() != null) {
                sorts.add(
                    filters.getPriceSort() == SortType.ASC
                        ? cb.asc(root.get("price"))
                        : cb.desc(root.get("price"))
                );
            }
            if (filters.getExperienceSort() != null) {
                sorts.add(
                    filters.getExperienceSort() == SortType.ASC
                        ? cb.asc(masterInfo.get("experience"))
                        : cb.desc(masterInfo.get("experience"))
                );
            }
            if (filters.getMinOrders() != null) {
                sorts.add(
                    filters.getOrdersCountSort() == SortType.ASC
                        ? cb.asc(cb.countDistinct(orders))
                        : cb.desc(cb.countDistinct(orders))
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
