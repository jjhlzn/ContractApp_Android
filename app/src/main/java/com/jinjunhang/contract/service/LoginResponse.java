package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginResponse extends ServerResponse {

    private boolean mSuccess;
    private String  mErrMessage;
    private String  mName;
    private String  mDepartment;

    public boolean isSuccess() {
        return mSuccess;
    }

    public String getErrMessage() {
        return mErrMessage;
    }

    public String getName() {
        return mName;
    }

    public String getDepartment() {
        return mDepartment;
    }

    public void setSuccess(boolean success) {
        mSuccess = success;
    }

    public void setErrMessage(String errMessage) {
        mErrMessage = errMessage;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDepartment(String department) {
        mDepartment = department;
    }
}
