package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.FavouriteFilters;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FavouriteFilteringSpecification {

    public static Specification<JobEntity> filter(FavouriteFilters filters, UUID userId) {
        return (root, query, criteriaBuilder) -> {
            // Join с таблицей избранных для фильтрации по пользователю
            Join<JobEntity, FavouriteJobEntity> favouriteJoin = root.join("favouriteServices", JoinType.INNER);

            // Условие: услуга должна быть в избранном у текущего пользователя
            Predicate userPredicate = criteriaBuilder.equal(favouriteJoin.get("user").get("id"), userId);

            List<Predicate> predicates = new ArrayList<>();
            predicates.add(userPredicate);

            // Фильтр по поисковому запросу (название или описание)
            if (filters.getQuery() != null && !filters.getQuery().isEmpty()) {
                String searchPattern = "%" + filters.getQuery().toLowerCase() + "%";
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
                Predicate descriptionPredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern);
                predicates.add(criteriaBuilder.or(namePredicate, descriptionPredicate));
            }

            // Фильтр по категории
            if (filters.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), filters.getCategoryId()));
            }

            // Фильтр по цене (min)
            if (filters.getMinPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"),
                        BigDecimal.valueOf(filters.getMinPrice())
                ));
            }

            // Фильтр по цене (max)
            if (filters.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"),
                        BigDecimal.valueOf(filters.getMaxPrice())
                ));
            }

            // Сортировка
            if (filters.getPriceSort() != null) {
                Order priceOrder = filters.getPriceSort() == SortType.ASC ?
                        criteriaBuilder.asc(root.get("price")) :
                        criteriaBuilder.desc(root.get("price"));
                query.orderBy(priceOrder);
            } else if (filters.getOrdersCountSort() != null) {
                // Сортировка по количеству заказов требует подзапроса
                Subquery<Long> ordersCountSubquery = query.subquery(Long.class);
                Root<JobEntity> subRoot = ordersCountSubquery.from(JobEntity.class);
                Join<Object, Object> ordersJoin = subRoot.join("orders", JoinType.LEFT);

                ordersCountSubquery.select(criteriaBuilder.count(ordersJoin))
                        .where(criteriaBuilder.equal(subRoot.get("id"), root.get("id")));

                Expression<Long> ordersCount = ordersCountSubquery.getSelection();
                Order ordersOrder = filters.getOrdersCountSort() == SortType.ASC ?
                        criteriaBuilder.asc(ordersCount) :
                        criteriaBuilder.desc(ordersCount);
                query.orderBy(ordersOrder);
            } else if (filters.getCreatedAtSort() != null) {
                Order createdAtOrder = filters.getCreatedAtSort() == SortType.ASC ?
                        criteriaBuilder.asc(root.get("createdAt")) :
                        criteriaBuilder.desc(root.get("createdAt"));
                query.orderBy(createdAtOrder);
            } else {
                // Сортировка по умолчанию - по дате добавления в избранное
                query.orderBy(criteriaBuilder.desc(favouriteJoin.get("createdAt")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}