package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.OrderBasicInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class GetOrderBasicInfoResponse extends ServerResponse {

    private OrderBasicInfo mBasicInfo;

    public OrderBasicInfo getBasicInfo() {
        return mBasicInfo;
    }

    public void setBasicInfo(OrderBasicInfo basicInfo) {
        mBasicInfo = basicInfo;
    }
}
