package net.arville.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PredicateBuilder<T> {

    private List<FilterCriteria> criteriaList;
    private Root<T> model;
    private CriteriaBuilder cb;


    public PredicateBuilder(Root<T> model, CriteriaBuilder cb) {
        this.model = model;
        this.cb = cb;
        this.criteriaList = new ArrayList<>();
    }

    public PredicateBuilder<T> with(FilterCriteria criteria) {
        criteriaList.add(criteria);
        return this;
    }

    public PredicateBuilder<T> with(FilterCriteria... criterias) {
        criteriaList.addAll(Arrays.asList(criterias));
        return this;
    }

    public PredicateBuilder<T> with(List<FilterCriteria> criterias) {
        criteriaList.addAll(criterias);
        return this;
    }

    public Predicate build() {
        if (criteriaList.size() == 0) {
            return null;
        }

        List<Predicate> specs = criteriaList.stream()
                .map(criteria -> (new PredicateHelper<>(criteria, model, cb)).getPredicate())
                .collect(Collectors.toList());

        Predicate result = specs.get(0);

        for (int i = 1; i < criteriaList.size(); i++) {
            result = criteriaList.get(i).isOrOperation()
                    ? cb.or(result, specs.get(i))
                    : cb.and(result, specs.get(i));
        }

        return result;
    }
}
