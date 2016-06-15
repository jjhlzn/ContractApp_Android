package com.jinjunhang.contract.service;

import com.jinjunhang.framework.service.ServerRequest;

import java.util.Map;

/**
 * Created by lzn on 16/6/14.
 */
public class SearchProductRequest extends ServerRequest {

    private String mCodes;
    private String mUserId;

    public String getCodes() {
        return mCodes;
    }

    public void setCodes(String codes) {
        mCodes = codes;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> parameters = super.getParams();
        parameters.put("userid", mUserId);
        parameters.put("codes", mCodes);
        return parameters;
    }

    @Override
    public String getServiceUrl() {
        return ServiceConfiguration.searchProducstUrl();
    }

    @Override
    public Class getServerResponseClass() {
        return SearchProductResponse.class;
    }
}
