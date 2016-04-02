package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/3/23.
 */
public class Order implements Serializable {
    private String mBusinessPerson;
    private String mContractNo;
    private String mOrderNo;
    private double mAmount = 0.0;
    private String mGuestName;

    public String getBusinessPerson() {
        return mBusinessPerson;
    }

    public String getContractNo() {
        return mContractNo;
    }

    public String getOrderNo() {
        return mOrderNo;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getGuestName() {
        return mGuestName;
    }

    public void setBusinessPerson(String businessPerson) {
        mBusinessPerson = businessPerson;
    }

    public void setContractNo(String contractNo) {
        mContractNo = contractNo;
    }

    public void setOrderNo(String orderNo) {
        mOrderNo = orderNo;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public void setGuestName(String guestName) {
        mGuestName = guestName;
    }
}
