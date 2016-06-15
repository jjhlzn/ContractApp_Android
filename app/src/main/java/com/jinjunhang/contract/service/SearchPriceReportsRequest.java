package com.jinjunhang.contract.service;

import com.jinjunhang.framework.service.PagedServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/14.
 */
public class SearchPriceReportsRequest extends PagedServerRequest {

    private String mUserId;
    private String mStartDate;
    private String mEndDate;
    private String mKeyword;

    public String getUserId() {
        return mUserId;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> parameters = super.getParams();
        parameters.put("userid", mUserId);
        parameters.put("startDate", mStartDate);
        parameters.put("endDate", mEndDate);
        parameters.put("keyword", mKeyword);
        return parameters;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.searchPriceReportUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SearchPriceReportsResponse.class;
    }
}
