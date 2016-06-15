package com.jinjunhang.contract.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/15.
 */
public class SubmitReportRequest extends ServerRequest {

    private String mUserId;
    private String mCodesString;

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setCodesString(String codesString) {
        mCodesString = codesString;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getCodesString() {
        return mCodesString;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.submitReportUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SubmitReportResponse.class;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> parameters = super.getParams();
        parameters.put("userid", mUserId);
        parameters.put("codes", mCodesString);
        return parameters;
    }
}
