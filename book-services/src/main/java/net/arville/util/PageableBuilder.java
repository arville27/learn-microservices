package net.arville.util;

import net.arville.constant.ResponseConfig;
import net.arville.exception.IllegalPageNumber;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PageableBuilder {
    private List<String> sortFields;
    private Integer pageSize;
    private Integer pageNumber;
    private Sort.Direction sortType;
    private boolean isUsingDefaultField;

    public PageableBuilder(String defaultSortField) {
        this.sortFields = new ArrayList<>();
        this.sortFields.add(defaultSortField);
        this.pageSize = ResponseConfig.RESPONSE_PAGE_SIZE;
        this.pageNumber = 0;
        this.sortType = Sort.DEFAULT_DIRECTION;
        this.isUsingDefaultField = true;
    }

    public Pageable build() {
        String[] fields = new String[sortFields.size()];
        sortFields.toArray(fields);
        return PageRequest.of(pageNumber, pageSize, sortType, fields);
    }

    public PageableBuilder setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public PageableBuilder setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber - 1;
        if (this.pageNumber < 0) {
            throw new IllegalPageNumber();
        }
        return this;
    }

    public PageableBuilder addSortField(List<String> sortFields) {
        if (isUsingDefaultField) {
            this.sortFields.clear();
            this.sortFields.addAll(sortFields);
            isUsingDefaultField = false;
        } else {
            this.sortFields.addAll(sortFields);
        }
        return this;
    }

    public PageableBuilder addSortField(String field) {
        if (isUsingDefaultField) {
            this.sortFields.set(0, field);
            isUsingDefaultField = false;
        } else {
            this.sortFields.add(field);
        }
        return this;
    }

    public PageableBuilder setSortType(Sort.Direction sortType) {
        this.sortType = sortType;
        return this;
    }
}
