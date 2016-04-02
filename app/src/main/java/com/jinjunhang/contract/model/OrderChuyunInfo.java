package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/3/24.
 */
public class OrderChuyunInfo implements Serializable{

    private String mDetailNo;
    private String mDate;
    private double mAmount;

    public String getDetailNo() {
        return mDetailNo;
    }

    public String getDate() {
        return mDate;
    }

    public double getAmount() {
        return mAmount;
    }

    public void setDetailNo(String detailNo) {
        mDetailNo = detailNo;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }
}
