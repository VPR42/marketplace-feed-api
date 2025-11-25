package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.dto.CategoryEntityWithCount;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<CategoryEntityWithCount> findAllWithCount(String query,
                                                          SortType jobsCountSort) {
        StringBuilder hql = new StringBuilder("""
            SELECT new com.vpr42.marketplacefeedapi.model.dto.CategoryEntityWithCount(
                c,
                COUNT(j)
            )
            FROM CategoryEntity c
            LEFT JOIN JobEntity j ON j.category = c
        """);

        if (query != null && !query.isBlank()) {
            hql.append(" WHERE (:search IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')))");
        }

        hql.append(" GROUP BY c.id, c.name");

        if (jobsCountSort != null) {
            hql.append(" ORDER BY COUNT(j) ").append(jobsCountSort == SortType.ASC ? "ASC" : "DESC");
            hql.append(", c.id ASC");
        } else {
            hql.append(" ORDER BY c.id ASC");
        }

        TypedQuery<CategoryEntityWithCount> q = em.createQuery(hql.toString(), CategoryEntityWithCount.class);
        if (query != null && !query.isBlank()) {
            q.setParameter("search", query);
        }

        return q.getResultList();
    }

    public void saveAll(List<CategoryEntity> categories) {
        for (CategoryEntity c : categories) {
            em.persist(c);
        }
    }

    public Optional<CategoryEntity> findById(Integer id) {
        return Optional.of(em.find(CategoryEntity.class, id));
    }

    public long count() {
        String hql = """
                SELECT COUNT(c)
                FROM CategoryEntity c
                """;

        TypedQuery<Long> query = em.createQuery(hql, Long.class);
        return query.getSingleResult();
    }
}
