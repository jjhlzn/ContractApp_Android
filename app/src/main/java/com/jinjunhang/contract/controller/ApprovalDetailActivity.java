package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalDetailActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "审批详情";
    }

    @Override
    protected Fragment createFragment() {
        return new ApprovalDetailFragment();
    }


}
