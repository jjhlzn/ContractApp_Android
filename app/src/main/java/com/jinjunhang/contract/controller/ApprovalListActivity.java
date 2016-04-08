package com.jinjunhang.contract.controller;

import android.support.v4.app.Fragment;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalListActivity extends SingleFragmentActivity {

    @Override
    protected String getActivityTitle() {
        return "审批列表";
    }

    @Override
    protected Fragment createFragment() {
        return new ApprovalListFragment();
    }
}
