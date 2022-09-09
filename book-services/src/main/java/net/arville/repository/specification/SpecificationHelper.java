package net.arville.repository.specification;

import net.arville.enumeration.SpecificationOperation;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class SpecificationHelper<T> implements Specification<T> {
    private final FilterCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation() == SpecificationOperation.GREATER_THAN_OR_EQUAL) {
            if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.LESS_THAN_OR_EQUAL) {
            if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.GREATER_THAN) {
            if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return criteriaBuilder.greaterThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.LESS_THAN) {
            if (root.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return criteriaBuilder.lessThan(root.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.EQUAL) {
            return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation() == SpecificationOperation.LIKE) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            } else {
                throw new IllegalArgumentException();
            }
        }
        return null;
    }

    public SpecificationHelper(FilterCriteria filterCriteria) {
        this.criteria = filterCriteria;
    }
}

