package net.arville.util;

import net.arville.enumeration.SpecificationOperation;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;

public class PredicateHelper<T> {

    private FilterCriteria criteria;
    private Root<T> model;

    private CriteriaBuilder cb;

    public PredicateHelper(FilterCriteria criteria, Root<T> model, CriteriaBuilder cb) {
        this.criteria = criteria;
        this.model = model;
        this.cb = cb;
    }

    public Predicate getPredicate() {
        if (criteria.getOperation() == SpecificationOperation.GREATER_THAN_OR_EQUAL) {
            if (model.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return cb.greaterThanOrEqualTo(model.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return cb.greaterThanOrEqualTo(model.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.LESS_THAN_OR_EQUAL) {
            if (model.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return cb.lessThanOrEqualTo(model.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return cb.lessThanOrEqualTo(model.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.GREATER_THAN) {
            if (model.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return cb.greaterThan(model.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return cb.greaterThan(model.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.LESS_THAN) {
            if (model.get(criteria.getKey()).getJavaType() == LocalDateTime.class) {
                return cb.lessThan(model.get(criteria.getKey()), (LocalDateTime) criteria.getValue());
            } else {
                return cb.lessThan(model.get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation() == SpecificationOperation.EQUAL) {
            return cb.equal(model.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation() == SpecificationOperation.LIKE) {
            if (model.get(criteria.getKey()).getJavaType() == String.class) {
                return cb.like(model.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            } else {
                throw new IllegalArgumentException();
            }
        }
        return null;
    }
}
