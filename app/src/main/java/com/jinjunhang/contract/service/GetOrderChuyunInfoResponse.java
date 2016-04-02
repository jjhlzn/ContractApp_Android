package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.OrderChuyunInfo;

/**
 * Created by lzn on 16/3/24.
 */
public class GetOrderChuyunInfoResponse extends ServerResponse {

    private OrderChuyunInfo mChuyunInfo;

    public OrderChuyunInfo getChuyunInfo() {
        return mChuyunInfo;
    }

    public void setChuyunInfo(OrderChuyunInfo chuyunInfo) {
        mChuyunInfo = chuyunInfo;
    }
}
