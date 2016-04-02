package com.jinjunhang.contract.model;

import com.jinjunhang.contract.service.SearchOrderResponse;

import java.io.Serializable;

/**
 * Created by lzn on 16/3/24.
 * 合同基本信息
 */
public class OrderBasicInfo implements Serializable{

    private String  mTimeLimit;
    private String  mStartPort;
    private String  mDestPort;
    private String  mGetMoneyType;
    private String  mPriceRule;

    public String getTimeLimit() {
        return mTimeLimit;
    }

    public String getStartPort() {
        return mStartPort;
    }

    public String getDestPort() {
        return mDestPort;
    }

    public String getGetMoneyType() {
        return mGetMoneyType;
    }

    public String getPriceRule() {
        return mPriceRule;
    }

    public void setTimeLimit(String timeLimit) {
        mTimeLimit = timeLimit;
    }

    public void setStartPort(String startPort) {
        mStartPort = startPort;
    }

    public void setDestPort(String destPort) {
        mDestPort = destPort;
    }

    public void setGetMoneyType(String getMoneyType) {
        mGetMoneyType = getMoneyType;
    }

    public void setPriceRule(String priceRule) {
        mPriceRule = priceRule;
    }
}
