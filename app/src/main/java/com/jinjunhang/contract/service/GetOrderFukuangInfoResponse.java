package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.OrderPurchaseInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class GetOrderFukuangInfoResponse extends ServerResponse {

    private OrderPurchaseInfo mFukuangInfo;

    public OrderPurchaseInfo getFukuangInfo() {
        return mFukuangInfo;
    }

    public void setFukuangInfo(OrderPurchaseInfo fukuangInfo) {
        mFukuangInfo = fukuangInfo;
    }
}
