package net.arville.util;

import net.arville.enumeration.SpecificationOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CriteriaQueryBuilder<T> {
    private List<String> selectFields;
    private List<FilterCriteria> filters;
    private EntityManager em;
    private CriteriaBuilder cb;
    private CriteriaQuery<T> query;
    private Root<T> model;

    private Pageable pageable;

    private Class<T> type;

    public CriteriaQueryBuilder(EntityManager em, Class<T> type) {
        this.type = type;
        this.em = em;
        this.cb = this.em.getCriteriaBuilder();
        this.query = this.cb.createQuery(type);
        this.model = this.query.from(type);
        this.selectFields = new ArrayList<>();
        this.filters = new ArrayList<>();

        var listFields = type.getDeclaredFields();
        if (listFields.length == 0) throw new NoSuchFieldError();
        this.pageable = new PageableBuilder(listFields[0].getName()).build();
    }

    public CriteriaQueryBuilder<T> setPageable(Pageable pageable) {
        this.pageable = pageable;
        return this;
    }

    public CriteriaQueryBuilder<T> selects(String... fieldNames) {
        selectFields = Arrays.asList(fieldNames);
        return this;
    }

    public CriteriaQueryBuilder<T> select(String fieldName) {
        selectFields.add(fieldName);
        return this;
    }

    public CriteriaQueryBuilder<T> with(String key, SpecificationOperation operation, Object value, boolean isOrOperation) {
        filters.add(new FilterCriteria(key, operation, value, isOrOperation));
        return this;
    }

    public CriteriaQueryBuilder<T> with(String key, SpecificationOperation operation, Object value) {
        filters.add(new FilterCriteria(key, operation, value));
        return this;
    }

    public Page<T> execute() {
        var orders = pageable.getSort()
                .stream()
                .map(order -> order.getDirection().isAscending()
                        ? cb.asc(model.get(order.getProperty()))
                        : cb.desc(model.get(order.getProperty()))
                ).collect(Collectors.toList());

        query.orderBy(orders);

        PredicateBuilder<T> predicateBuilder = new PredicateBuilder<>(model, cb);
        Predicate filterExpression = predicateBuilder.with(filters).build();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<T> bookModelCount = countQuery.from(type);

        if (filterExpression != null) {
            query.where(filterExpression);
            countQuery.where(filterExpression).select(cb.count(bookModelCount));
        } else {
            countQuery.select(cb.count(bookModelCount));
        }

        if (selectFields.size() > 0) {
            List<Selection<?>> selectedFields = selectFields.stream()
                    .map(model::get)
                    .collect(Collectors.toList());
            query.multiselect(selectedFields);
        }

        List<T> results = em.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        Long resItemCount = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, resItemCount);
    }
}
