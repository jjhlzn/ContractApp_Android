package com.jinjunhang.contract.service;

import android.app.Service;

import com.jinjunhang.framework.service.PagedServerRequest;
import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/14.
 */
public class GetPriceReportRequest extends PagedServerRequest {

    private String mReportId;
    private String mUserId;

    public String getReportId() {
        return mReportId;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setReportId(String reportId) {
        mReportId = reportId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.getPriceReportUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return GetPriceReportResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> parameters =  super.getParams();
        parameters.put("reportid", mReportId);
        parameters.put("userid", mUserId);
        return parameters;
    }
}
