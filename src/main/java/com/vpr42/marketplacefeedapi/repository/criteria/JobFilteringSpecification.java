package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.OrderEntity;
import com.vpr42.marketplacefeedapi.model.entity.SkillEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class JobFilteringSpecification {

    public static Specification<JobEntity> filter(JobFilters filters) {
        return (root, query, cb) -> {
            query.select(root.get("id"));

            // Join-ы
            Join<JobEntity, UserEntity> user = root.join("user");
            Join<JobEntity, CategoryEntity> category = null;
            Join<UserEntity, MasterInfoEntity> master = user.join("masterInfo", JoinType.LEFT);
            Join<UserEntity, CityEntity> city = null;
            Join<JobEntity, OrderEntity> orders = null;

            if (filters.getCategoryId() != null && filters.getCategoryId() > 0) {
                category = root.join("category");
            }

            if (filters.getCityId() != null && filters.getCityId() > 0) {
                city = user.join("city");
            }

            if (filters.getOrdersCountSort() != null
                    || filters.getMinOrders() != null && filters.getMinOrders() > 0) {
                orders = root.join("orders", JoinType.LEFT);
            }

            // Where
            List<Predicate> whereStatements = new ArrayList<>();
            if (filters.getMasterId() != null) {
                whereStatements.add(
                    cb.equal(
                        master.get("id"),
                        filters.getMasterId()
                    )
                );
            }
            if (filters.getCategoryId() != null
                    && filters.getCategoryId() > 0) {
                whereStatements.add(
                    cb.equal(
                        category.get("id"),
                        filters.getCategoryId()
                    )
                );
            }
            if (filters.getExperience() != null
                    && filters.getExperience() > 0) {
                whereStatements.add(
                    cb.greaterThanOrEqualTo(
                        cb.coalesce(master.get("experience"), 0L),
                        (long) filters.getExperience()
                    )
                );
            }
            if (filters.getCityId() != null
                && filters.getCityId() > 0) {
                whereStatements.add(
                    cb.equal(
                        city.get("id"),
                        filters.getCityId()
                    )
                );
            }
            if (filters.getMinPrice() != null || filters.getMaxPrice() != null) {
                int min = filters.getMinPrice() != null
                        ? filters.getMinPrice()
                        : 0;
                int max = filters.getMaxPrice() != null
                        ? filters.getMaxPrice()
                        : Integer.MAX_VALUE;
                if (min < max) {
                    whereStatements.add(
                            cb.between(
                                    root.get("price"),
                                    min,
                                    max
                            )
                    );
                }
            }
            if (filters.getQuery() != null && !filters.getQuery().isBlank()) {
                String q = "%" + filters.getQuery().toLowerCase() + "%";

                whereStatements.add(
                    cb.or(
                        cb.like(
                            cb.lower(root.get("name")),
                            q
                        ),
                        cb.like(
                            cb.lower(cb.coalesce(
                                master.get("pseudonym"),
                                ""
                            )),
                            q
                        )
                    )
                );
            }
            if (filters.getTags() != null && filters.getTags().length > 0) {
                Subquery<UUID> tagsSubquery = query.subquery(UUID.class);
                Root<JobEntity> subroot = tagsSubquery.from(JobEntity.class);
                Join<JobEntity, TagEntity> tagsJoin = subroot.join("tags");
                tagsSubquery.select(subroot.get("id"))
                        .where(
                            tagsJoin.get("name").in(Arrays.asList(filters.getTags()))
                        )
                        .groupBy(subroot.get("id"))
                        .having(cb.equal(
                            cb.countDistinct(tagsJoin.get("name")),
                            filters.getTags().length
                        ));

                whereStatements.add(
                    root.get("id").in(tagsSubquery)
                );
            }
            if (filters.getSkills() != null && filters.getSkills().length > 0) {
                Subquery<UUID> skillsSubquery = query.subquery(UUID.class);
                Root<JobEntity> subroot = skillsSubquery.from(JobEntity.class);
                Join<JobEntity, UserEntity> subrootUser = subroot.join("user");
                Join<UserEntity, MasterInfoEntity> subrootMaster = subrootUser.join("masterInfo", JoinType.INNER);
                Join<MasterInfoEntity, SkillEntity> skillsJoin = subrootMaster.join("skills", JoinType.INNER);
                skillsSubquery.select(subroot.get("id"))
                        .where(cb.and(skillsJoin.get("name").isNotNull(),
                                skillsJoin.get("name")
                                .in(Arrays.asList(filters.getTags())))
                        )
                        .groupBy(subroot.get("id"))
                        .having(cb.equal(
                                cb.countDistinct(skillsJoin.get("name")),
                                filters.getSkills().length
                        ));

                whereStatements.add(
                    root.get("id").in(skillsSubquery)
                );
            }

            // Группировка
            if (orders != null) {
                query.groupBy(
                    root.get("id"),
                    root.get("price"),
                    master.get("experience")
                );
                query.having(
                    cb.greaterThanOrEqualTo(
                        cb.countDistinct(
                            orders.get("id")
                        ),
                        (long) filters.getMinOrders()
                    )
                );
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
                        ? cb.asc(master.get("experience"))
                        : cb.desc(master.get("experience"))
                );
            }
            if (filters.getOrdersCountSort() != null) {
                Expression<Long> ordersCount = cb.countDistinct(orders.get("id"));
                sorts.add(
                    filters.getOrdersCountSort() == SortType.ASC
                        ? cb.asc(ordersCount)
                        : cb.desc(ordersCount)
                );
            }

            if (!sorts.isEmpty()) {
                query.orderBy(sorts);
            }

            return cb.and(whereStatements.toArray(new Predicate[0]));
        };
    }
}
