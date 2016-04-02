package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.OrderShouhuiInfo;

/**
 * Created by lzn on 16/3/25.
 */
public class GetOrderShouhuiInfoResponse extends ServerResponse {

    private OrderShouhuiInfo mShouhuiInfo;

    public OrderShouhuiInfo getShouhuiInfo() {
        return mShouhuiInfo;
    }

    public void setShouhuiInfo(OrderShouhuiInfo shouhuiInfo) {
        mShouhuiInfo = shouhuiInfo;
    }
}
