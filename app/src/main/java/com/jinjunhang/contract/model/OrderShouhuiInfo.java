package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/3/24.
 * 收汇信息
 */
public class OrderShouhuiInfo implements Serializable {

    private String mDate;
    private double mAmount;

    public String getDate() {
        return mDate;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }
}
