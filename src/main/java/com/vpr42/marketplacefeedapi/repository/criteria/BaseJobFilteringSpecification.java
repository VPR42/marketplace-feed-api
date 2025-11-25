package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.OrderEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.criteria.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class BaseJobFilteringSpecification {

    protected static void applyCommonFilters(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            Integer categoryId,
            Integer minPrice,
            Integer maxPrice,
            String searchQuery,
            String[] tags,
            String[] skills,
            SortType priceSort,
            SortType ordersCountSort,
            SortType createdAtSort,
            Integer minOrders) {

        // Join-ы
        Join<JobEntity, MasterInfoEntity> master = root.join("masterInfo");
        Join<JobEntity, CategoryEntity> category = null;
        Join<JobEntity, OrderEntity> orders = null;

        if (categoryId != null && categoryId > 0) {
            category = root.join("category");
        }
        if (ordersCountSort != null || (minOrders != null && minOrders > 0)) {
            orders = root.join("orders", JoinType.LEFT);
        }

        // Where
        List<Predicate> whereStatements = new ArrayList<>();

        // Фильтр по категории
        if (categoryId != null && categoryId > 0) {
            whereStatements.add(
                    cb.equal(
                            category.get("id"),
                            categoryId
                    )
            );
        }
        // Фильтр по цене
        if (minPrice != null || maxPrice != null) {
            int min = minPrice != null ? minPrice : 0;
            int max = maxPrice != null ? maxPrice : Integer.MAX_VALUE;
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
        // Фильтр по поисковому запросу
        if (searchQuery != null && !searchQuery.isBlank()) {
            String q = "%" + searchQuery.toLowerCase() + "%";

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
        // Фильтр по тегам
        if (tags != null && tags.length > 0) {
            Subquery<Object> tagsSubquery = createTagsSubquery(root, query, cb, tags);
            whereStatements.add(root.get("id").in(tagsSubquery));
        }
        // Фильтр по навыкам
        if (skills != null && skills.length > 0) {
            Subquery<Object> skillsSubquery = createSkillsSubquery(root, query, cb, skills);
            whereStatements.add(root.get("id").in(skillsSubquery));
        }
        // Группировка для подсчета заказов
        if (orders != null && minOrders != null && minOrders > 0) {
            query.groupBy(root.get("id"), root.get("price"));
            query.having(
                    cb.greaterThanOrEqualTo(
                            cb.countDistinct(orders.get("id")),
                            (long) minOrders
                    )
            );
        }
        // Сортировка
        applySorting(root, query, cb, orders, priceSort, ordersCountSort, createdAtSort);

        if (!whereStatements.isEmpty()) {
            query.where(cb.and(whereStatements.toArray(new Predicate[0])));
        }
    }

    protected static void applyFavouriteCondition(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            UUID userId) {
        Subquery<UUID> favouriteSubquery = query.subquery(UUID.class);
        Root<FavouriteJobEntity> favouriteRoot = favouriteSubquery.from(FavouriteJobEntity.class);
        favouriteSubquery.select(favouriteRoot.get("service").get("id"))
                .where(cb.equal(favouriteRoot.get("user").get("id"), userId));

        Predicate existingRestriction = query.getRestriction();
        Predicate favouritePredicate = root.get("id").in(favouriteSubquery);

        if (existingRestriction != null) {
            query.where(cb.and(existingRestriction, favouritePredicate));
        } else {
            query.where(favouritePredicate);
        }
    }

    protected static void applyFavouriteDefaultSorting(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            UUID userId) {
        if (query.getOrderList() == null || query.getOrderList().isEmpty()) {
            Subquery<java.time.Instant> favouriteDateSubquery = query.subquery(java.time.Instant.class);
            Root<FavouriteJobEntity> favouriteDateRoot = favouriteDateSubquery.from(FavouriteJobEntity.class);
            favouriteDateSubquery.select(favouriteDateRoot.get("createdAt"))
                    .where(cb.and(
                            cb.equal(favouriteDateRoot.get("user").get("id"), userId),
                            cb.equal(favouriteDateRoot.get("service").get("id"), root.get("id"))
                    ));
            query.orderBy(cb.desc(favouriteDateSubquery));
        }
    }

    private static Subquery<Object> createTagsSubquery(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            String[] tags) {
        Subquery<Object> tagsSubquery = query.subquery(Object.class);
        Root<JobEntity> subroot = tagsSubquery.from(JobEntity.class);
        Join<JobEntity, TagEntity> tagsJoin = subroot.join("tags");

        tagsSubquery.select(subroot.get("id"))
                .where(tagsJoin.get("name").in(Arrays.asList(tags)))
                .groupBy(subroot.get("id"))
                .having(cb.equal(
                        cb.countDistinct(tagsJoin.get("name")),
                        tags.length
                ));
        return tagsSubquery;
    }

    private static Subquery<Object> createSkillsSubquery(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            String[] skills) {
        Subquery<Object> skillsSubquery = query.subquery(Object.class);
        Root<JobEntity> subroot = skillsSubquery.from(JobEntity.class);
        Join<JobEntity, MasterInfoEntity> subrootMaster = subroot.join("masterInfo");
        Join<MasterInfoEntity, Object> skillsJoin = subrootMaster.join("skills", JoinType.LEFT);

        skillsSubquery.select(subroot.get("id"))
                .where(cb.and(
                        skillsJoin.get("name").isNotNull(),
                        skillsJoin.get("name").in(Arrays.asList(skills))
                ))
                .groupBy(subroot.get("id"))
                .having(cb.equal(
                        cb.countDistinct(skillsJoin.get("name")),
                        skills.length
                ));
        return skillsSubquery;
    }

    private static void applySorting(
            Root<JobEntity> root,
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            Join<JobEntity, OrderEntity> orders,
            SortType priceSort,
            SortType ordersCountSort,
            SortType createdAtSort) {
        List<Order> sorts = new ArrayList<>();

        // Сортировка по цене
        if (priceSort != null) {
            sorts.add(
                    priceSort == SortType.ASC
                            ? cb.asc(root.get("price"))
                            : cb.desc(root.get("price"))
            );
        }
        // Сортировка по количеству заказов
        if (ordersCountSort != null && orders != null) {
            Expression<Long> ordersCount = cb.countDistinct(orders.get("id"));
            sorts.add(
                    ordersCountSort == SortType.ASC
                            ? cb.asc(ordersCount)
                            : cb.desc(ordersCount)
            );
        }
        // Сортировка по дате создания
        if (createdAtSort != null) {
            sorts.add(
                    createdAtSort == SortType.ASC
                            ? cb.asc(root.get("createdAt"))
                            : cb.desc(root.get("createdAt"))
            );
        }
        if (!sorts.isEmpty()) {
            query.orderBy(sorts);
        }
    }
}