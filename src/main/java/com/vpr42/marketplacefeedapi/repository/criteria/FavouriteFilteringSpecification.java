package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.FavouriteFilters;
import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FavouriteFilteringSpecification extends BaseJobFilteringSpecification {

    public static Specification<JobEntity> filter(FavouriteFilters filters, UUID userId) {
        return (root, query, cb) -> {
            query.select(root.get("id"));

            // Join с таблицей избранных
            Join<JobEntity, FavouriteJobEntity> favouriteJoin = root.join("favouriteServices", JoinType.INNER);

            List<Predicate> whereStatements = new ArrayList<>();
            whereStatements.add(cb.equal(favouriteJoin.get("user").get("id"), userId));

            applyCommonFilters(
                    root, query, cb,
                    filters.getCategoryId(),
                    filters.getMinPrice(),
                    filters.getMaxPrice(),
                    filters.getQuery(),
                    filters.getTags(),
                    null,
                    filters.getPriceSort(),
                    filters.getOrdersCountSort(),
                    filters.getCreatedAtSort(),
                    null
            );
            applyFavouriteDefaultSorting(query, cb, favouriteJoin);

            Predicate commonPredicate = query.getRestriction();
            Predicate favouritePredicate = cb.and(whereStatements.toArray(new Predicate[0]));
            query.where(cb.and(commonPredicate, favouritePredicate));
            return query.getRestriction();
        };
    }

    private static void applyFavouriteDefaultSorting(
            CriteriaQuery<?> query,
            CriteriaBuilder cb,
            Join<JobEntity, FavouriteJobEntity> favouriteJoin) {
        if (query.getOrderList() == null || query.getOrderList().isEmpty()) {
            query.orderBy(cb.desc(favouriteJoin.get("createdAt")));
        }
    }
}