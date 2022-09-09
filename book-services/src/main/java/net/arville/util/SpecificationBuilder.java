package net.arville.util;

import net.arville.enumeration.SpecificationOperation;
import net.arville.repository.specification.FilterCriteria;
import net.arville.repository.specification.SpecificationHelper;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SpecificationBuilder<T> {
    private final List<FilterCriteria> params;

    public SpecificationBuilder() {
        params = new ArrayList<>();
    }

    public SpecificationBuilder<T> with(String key, SpecificationOperation operation, Object value) {
        params.add(new FilterCriteria(key, operation, value));
        return this;
    }

    public SpecificationBuilder<T> with(String key, SpecificationOperation operation, Object value, boolean isOrOperation) {
        params.add(new FilterCriteria(key, operation, value, isOrOperation));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<T>> specs = params.stream()
                .map(SpecificationHelper<T>::new)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrOperation()
                    ? Specification.where(result).or(specs.get(i))
                    : Specification.where(result).and(specs.get(i));
        }

        return result;
    }
}
