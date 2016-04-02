package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/4/2.
 */
public class OrderQueryObject extends QueryObject {
    private String mKeyword;
    private String mStartDate;
    private String mEndDate;
    private int index;
    private int pageSize;

    public String getKeyword() {
        return mKeyword;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public int getIndex() {
        return index;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
