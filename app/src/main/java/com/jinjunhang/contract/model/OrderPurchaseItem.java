package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderPurchaseItem implements Serializable{
    private String mContract;
    private String mDate;
    private String mFactory;
    private double mAmount;

    public String getContract() {
        return mContract;
    }

    public String getDate() {
        return mDate;
    }

    public String getFactory() {
        return mFactory;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setContract(String contract) {
        mContract = contract;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setFactory(String factory) {
        mFactory = factory;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }
}
