package com.vpr42.marketplacefeedapi.repository.criteria;

import com.vpr42.marketplacefeedapi.model.dto.FavouriteFilters;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class FavouriteFilteringSpecification extends BaseJobFilteringSpecification {

    public static Specification<JobEntity> filter(FavouriteFilters filters, UUID userId) {
        return (root, query, cb) -> {
            query.select(root.get("id"));

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

            applyFavouriteCondition(root, query, cb, userId);
            applyFavouriteDefaultSorting(root, query, cb, userId);
            return query.getRestriction();
        };
    }
}