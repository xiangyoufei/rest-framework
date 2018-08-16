package com.example.demo.dto;


import java.util.List;

import com.example.demo.jdbc.paging.Paging;

/**
 * 分页结果对象
 *
 * @author huangyong
 * @since 1.0.0
 */
public class PagingResult<T> {

    private List<T> rows;

    private long total;

    // ----------------------------------------------------------------------------------------------------

    public PagingResult(Paging<T> paging) {
        rows = paging.getItemList();
        total = paging.getTotalCount();
    }

    // ----------------------------------------------------------------------------------------------------

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
