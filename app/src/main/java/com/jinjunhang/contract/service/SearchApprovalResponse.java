package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Approval;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzn on 16/4/3.
 */
public class SearchApprovalResponse extends ServerResponse {

    private int mTotalNumber;
    private List<Approval> mApprovals;

    public SearchApprovalResponse() {
        mApprovals = new ArrayList<Approval>();
        mTotalNumber = 0;
    }

    public List<Approval> getApprovals() {
        return mApprovals;
    }

    public void setApprovals(List<Approval> approvals) {
        mApprovals = approvals;
    }

    public int getTotalNumber() {
        return mTotalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        mTotalNumber = totalNumber;
    }

    public void addApproval(Approval approval) {
        mApprovals.add(approval);
    }
}
