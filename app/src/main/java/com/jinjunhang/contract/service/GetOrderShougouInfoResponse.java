package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.OrderPurchaseInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class GetOrderShougouInfoResponse extends ServerResponse{
    private OrderPurchaseInfo mShougouInfo;

    public OrderPurchaseInfo getShougouInfo() {
        return mShougouInfo;
    }

    public void setShougouInfo(OrderPurchaseInfo shougouInfo) {
        mShougouInfo = shougouInfo;
    }
}
