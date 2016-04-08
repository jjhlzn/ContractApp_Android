package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/4/7.
 */
public class AuditApprovalResponse extends ServerResponse {

    private boolean mResult;
    private String  mMessage;

    public boolean isResult() {
        return mResult;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setResult(boolean result) {
        mResult = result;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
