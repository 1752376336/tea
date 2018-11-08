package com.tea.framework.metadata.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis用分页对象 继承Array list
 */
@Data
public class PageWrapper<E> extends ArrayList<E> implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 总条数
     */
    protected int totalCount;
    /**
     * 总页数
     */
    protected int totalPage;
    /**
     * 页码 从1开始
     */
    private int pageNo;
    /**
     * 每页条数 从1开始
     */
    private int pageSize = 10;


    public List<E> getResults() {
        return this;
    }

    public void calc() {
        if (this.getPageSize() > 0) {
            this.setTotalPage(this.getTotalCount() % this.getPageSize() == 0 ?
                    this.getTotalCount() / this.getPageSize() : this.getTotalCount() / this.getPageSize() + 1);
        }
    }

    public Page<E> toPage() {
        this.calc();
        Page<E> page = new Page<>();
        page.setResults(this.getResults());
        page.setPageNo(this.getPageNo());
        page.setPageSize(this.getPageSize());
        page.setTotalCount(this.getTotalCount());
        page.setTotalPage(this.getTotalPage());
        page.setFirstPage(this.getPageNo() == 1);
        page.setLastPage(this.getPageNo() == this.getTotalPage());
        page.setStartRow((this.getPageNo() - 1) * this.getPageSize());
        page.setEndRow(this.getPageNo() * this.getPageSize());
        page.setHasPrevPage(this.getPageNo() - 1 > 0);
        page.setPrevPage(this.getPageNo() - 1);
        page.setHasNextPage(this.getPageNo() + 1 <= this.getTotalPage());
        page.setNextPage(this.getPageNo() + 1);
        return page;
    }
}
