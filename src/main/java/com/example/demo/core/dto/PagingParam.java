package com.example.demo.core.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * 分页参数对象
 *
 * @author huangyong
 * @since 1.0.0
 */
public class PagingParam {

    private int offset;

    private int limit;

    private String search;

    private String sort;

    private String order;

    // ----------------------------------------------------------------------------------------------------

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    // ----------------------------------------------------------------------------------------------------

    public int getPagingNumber() {
        if (limit == 0) {
            return 1;
        }
        return offset / limit + 1;
    }

    public int getPageSize() {
        return limit;
    }

    public String getWhereCondition(String columnName) {
        return StringUtils.isNotEmpty(search) ? columnName + " LIKE '%" + search + "%'" : null;
    }

    public String getOrderBy() {
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(order)) {
            return null;
        }
        return sort + " " + order.toUpperCase();
    }
}
