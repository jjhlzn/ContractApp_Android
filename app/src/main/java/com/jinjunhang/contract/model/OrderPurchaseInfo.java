package com.jinjunhang.contract.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/3/24.
 * //合同收购信息, 工厂付款信息
 */
public class OrderPurchaseInfo implements Serializable {

    private List<OrderPurchaseItem> mItems = new ArrayList<OrderPurchaseItem>();

    public List<OrderPurchaseItem> getItems() {
        return mItems;
    }


    public void setItems(List<OrderPurchaseItem> items) {
        mItems = items;
    }
}
