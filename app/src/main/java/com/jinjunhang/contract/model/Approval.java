package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/4/6.
 */
public class Approval implements Serializable {

    private String mId;
    private String mApprovalObject;
    private String mKeyword;
    private String mType;
    private double mAmount;
    private String mReporter;
    private String mReportDate;
    private String mStatus;
    private String mApprovalResult;

    public String getId() {
        return mId;
    }

    public String getApprovalObject() {
        return mApprovalObject;
    }

    public String getKeyword() {
        return mKeyword;
    }

    public String getType() {
        return mType;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getReporter() {
        return mReporter;
    }

    public String getReportDate() {
        return mReportDate;
    }

    public String getStatus() {
        return mStatus;
    }

    public String getApprovalResult() {
        return mApprovalResult;
    }

    public void setId(String id) {
        mId = id;
    }

    public void setApprovalObject(String approvalObject) {
        mApprovalObject = approvalObject;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
    }

    public void setType(String type) {
        mType = type;
    }

    public void setAmount(double amount) {
        mAmount = amount;
    }

    public void setReporter(String reporter) {
        mReporter = reporter;
    }

    public void setReportDate(String reportDate) {
        mReportDate = reportDate;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public void setApprovalResult(String approvalResult) {
        mApprovalResult = approvalResult;
    }
}
