package net.arville.repository.specification;

import net.arville.enumeration.SpecificationOperation;

public class FilterCriteria {
    private final String key;
    private final SpecificationOperation operation;
    private final Object value;

    private final boolean isOrOperation;

    public String getKey() {
        return key;
    }

    public SpecificationOperation getOperation() {
        return operation;
    }

    public Object getValue() {
        return value;
    }

    public boolean isOrOperation() {
        return isOrOperation;
    }

    public FilterCriteria(String key, SpecificationOperation operation, Object value) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isOrOperation = false;
    }

    public FilterCriteria(String key, SpecificationOperation operation, Object value, boolean isOrOperation) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.isOrOperation = isOrOperation;
    }
}
