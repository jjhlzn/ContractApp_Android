package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/3.
 */
public class ApprovalSearchActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ApprovalSearchFragment();
    }

    @Override
    protected String getActivityTitle() {
        return "审批";
    }
}
