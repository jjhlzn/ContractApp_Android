package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalQueryObject extends QueryObject {

    private boolean mContainApproved;
    private boolean mContainUnapproved;
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

    public boolean isContainApproved() {
        return mContainApproved;
    }

    public boolean isContainUnapproved() {
        return mContainUnapproved;
    }

    public void setContainApproved(boolean containApproved) {
        mContainApproved = containApproved;
    }

    public void setContainUnapproved(boolean containUnapproved) {
        mContainUnapproved = containUnapproved;
    }
}
