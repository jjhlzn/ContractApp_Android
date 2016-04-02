package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/3/23.
 */
public class SearchOrderResponse extends ServerResponse {
    private int mTotalNumber = 0;
    private List<Order> mOrders = new ArrayList<Order>();

    public int getTotalNumber() {
        return mTotalNumber;
    }

    public List<Order> getOrders() {
        return mOrders;
    }

    public void addOrder(Order order) {
        mOrders.add(order);
    }

    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    public void setOrders(List<Order> orders) {
        mOrders = orders;
    }
}