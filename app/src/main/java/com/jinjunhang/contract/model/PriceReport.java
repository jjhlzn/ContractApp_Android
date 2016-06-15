package com.jinjunhang.contract.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/6/14.
 */
public class PriceReport extends BaseModelObject {

    private String mId;
    private String mDate;
    private String mReporter;
    private String mStatus;
    private String mDetailInfo;

    private List<Product> products = new ArrayList<Product>();

    public String getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public String getReporter() {
        return mReporter;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getDetailInfo() {
        return mDetailInfo;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public void setReporter(String reporter) {
        mReporter = reporter;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setDetailInfo(String detailInfo) {
        mDetailInfo = detailInfo;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
